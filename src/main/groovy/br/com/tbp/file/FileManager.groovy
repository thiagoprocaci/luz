package br.com.tbp.file


class FileManager {

    def save(String text) {
        def date = new Date()
        def file = new File("files" +  File.separator + "fb.gml")
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(text.getBytes());
        fop.flush();
        fop.close();
    }
}
