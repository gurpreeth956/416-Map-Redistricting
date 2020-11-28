package com.giants;

import java.io.*;

public class Script {

    /**
     * This method creates a script in a temporary file and runs the script in a process
     *
     * @param cmd - String of the command
     * @return The process output
     */
    public String createScript(String cmd) {
        String processOutput = null;
        try {
            File tempScript = File.createTempFile("script", null, new File("./src/main/resources/scripts"));
            FileWriter fw = new FileWriter(tempScript);
            fw.write(cmd);
            fw.close();
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.redirectErrorStream(true);
            Process process = pb.start();
            processOutput = getProcessOutput(process);
            tempScript.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processOutput;
    }

    /**
     * This method redirects the processes output from SeaWulf and stores in a variables
     *
     * @param process - The process
     * @return String with the process output
     */
    public String getProcessOutput(Process process) {
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator")); // Returns OS dependent line separator
            }
            result = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
