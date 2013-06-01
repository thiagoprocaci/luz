package br.com.tbp.file


class FileManager {

    private static final String FILES_FOLDER = "files"

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
        return write(getFile("fb.gml"), text)

    }

    def File save(String text, String fileName) {
        return write(getFile(fileName), text)
    }

    private File getFile(String path) {
        def folder = new File(FILES_FOLDER)
        if(!folder.exists()) {
            folder.mkdir()
        }
        def file = new File(folder.getAbsolutePath(), path)
        if(!file.exists()) {
            file.createNewFile();
        }
        return file
    }

}
