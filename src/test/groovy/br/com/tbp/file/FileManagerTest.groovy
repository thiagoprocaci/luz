package br.com.tbp.file
import br.com.tbp.support.FileReader


class FileManagerTest extends GroovyTestCase {

    void testSave() {
        FileManager fileManager = new FileManager()
        fileManager.save("teste")
        String path = "files" +  File.separator + "fb.gml"
        def file = new File(path)

        assertTrue(file.exists())
        String content = FileReader.readFile(path)
        assertEquals("teste", content.trim())
    }
}
