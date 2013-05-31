package br.com.tbp.file


class FileManager {

    private static final String FILES = "files"

    private File write(File file, String text) {
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        try {
            out.write(text);
        } finally {
            out.flush();
            out.close();
        }
        return file
    }

    def File save(String text) {
        def file = new File(FILES + File.separator + "fb.gml")
        return write(file, text)

    }

    def File save(String text, String fileName) {
        def file = new File(FILES + File.separator + fileName)
        return write(file, text)
    }

}
