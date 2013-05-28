package br.com.tbp.file


class FileManager {

    def save(String text) {
        def file = new File("files" +  File.separator + "fb.gml")
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(text.getBytes());
        fop.flush();
        fop.close();
    }

    def save(String text, String fileName) {
        def file = new File("files" +  File.separator + fileName)
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(text.getBytes());
        fop.flush();
        fop.close();
    }
}
