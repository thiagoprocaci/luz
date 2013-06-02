package br.com.tbp.crawler

import br.com.tbp.file.FileManager
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.JSON

class FBCrawler {

    private String getFBUrl() {
        String url = "https://graph.facebook.com/384634338233143/feed?&access_token="
        String accessToken = br.com.tbp.file.FileReader.readFile("src/main/resources/fb_access_token.txt")
        return url + accessToken.trim()
    }

    public String getFBData() {
        def url = getFBUrl()
        int i = 0
        StringBuffer buffer = new StringBuffer()
        buffer.append("{ \"fb\" :[ \n")
        while (url != null && !url.isEmpty()) {
            def http = new HTTPBuilder( url )

            http.request( GET, JSON ) {
                // response handler for a success response code:
                response.success = { resp, json ->
                   buffer.append(json.toString())
                   if(json.paging != null && json.paging.next != null) {
                       buffer.append("\n , \n")
                       url = json.paging.next
                   } else {
                       url = null
                   }
                }

                response.error = { resp
                    url = null
                }
            }
            i++
        }
        buffer.append("]}")
        FileManager fileManager = new FileManager()
        fileManager.save(buffer.toString(), "fb_feed.txt")
        return buffer.toString()
    }

    public static void main(String... s) {
        FBCrawler fbCrawler = new FBCrawler()
        fbCrawler.getFBData()
    }


}
