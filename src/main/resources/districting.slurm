#!/usr/bin/env bash

#SBATCH --job-name=Districting
#SBATCH --output=districting.log
#SBATCH --ntasks-per-node=24
#SBATCH --nodes=2
#SBATCH --time=00:05:00
#SBATCH -p short-24core

echo I am using the following nodes: $SLURM_JOB_NODELIST
echo My job ID is: $SLURM_JOB_ID

cat << EOF > args_test.py

import os
import sys
import time
from mpi4py import MPI

start_time = time.time()

comm = MPI.COMM_WORLD
size = comm.Get_size()
rank = comm.Get_rank()

command = "python arguments.py {} {} {} {} {} {}".format(sys.argv[1], rank, sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])
os.system(command)

comm.barrier()
print("Completed in {:.2f}s for rank {}:".format(time.time() - start_time, rank))
EOF

mpirun -np 48 python args_test.py $SLURM_JOB_ID $1 $2 $3 $4
