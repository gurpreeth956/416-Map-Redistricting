#!/usr/bin/env bash

#SBATCH --job-name=Districting
#SBATCH --output=districting.log
#SBATCH --ntasks-per-node=24
#SBATCH --nodes=2
#SBATCH --time=01:00:00
#SBATCH -p short-24core

echo I am using the following nodes: $SLURM_JOB_NODELIST
echo My job ID is: $SLURM_JOB_ID

cat << EOF > RunAlgo.py

import os
import sys
import time
from mpi4py import MPI

start_time = time.time()

comm = MPI.COMM_WORLD
size = comm.Get_size()
rank = comm.Get_rank()

# Split number of districtings between 48 nodes (some nodes will have to do +1 more)
numOfMaps = int(sys.argv[5])
nodeBoundary = numOfMaps % 48
numOfMapsForThisNode = (numOfMaps - nodeBoundary) / 48
if rank < nodeBoundary:
    numOfMapsForThisNode += 1
if numOfMapsForThisNode != 0:
    command = "python SeedDistricting.py {} {} {} {} {} {}".format(sys.argv[1], rank, sys.argv[2], sys.argv[3], sys.argv[4], numOfMapsForThisNode)
    os.system(command)

comm.barrier()
print("Completed in {:.2f}s for rank {}:".format(time.time() - start_time, rank))
EOF

mpirun -np 48 python RunAlgo.py $1 $2 $3 $4 $5
