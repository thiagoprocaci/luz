package br.com.tbp.file

class FileReader {

    private static String read(File file) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new java.io.FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {
                buffer.append(sCurrentLine);
                buffer.append("\n");
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return buffer.toString();
    }

    public static String readFile(String path) {
        path = path.replace("/", File.separator);
        File file = new File(path);
        return read(file);
    }

}
