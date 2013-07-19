# -*- coding: utf-8 -*-
import sys
import urllib
import json
import codecs

def load_data():
    token = 'CAACEdEose0cBAJRuYCLZCTKnPxt9OG07Bk6IWmCD6xqZCaNgDDs22Qo9yCVPkDxPKHDPwiTZCznfY6o89lraRhE49oZAYEnpSRJMuabor00hPCt8ZBZBBd0HjW5oRp0CE8cOpgVTB4imvvOd5YyTfv122xIGth7vmDTZBYLbZC5WqQZDZD'
    url = "https://graph.facebook.com/2204806663/feed?&access_token=" + token
    index = 0
    with codecs.open("test_output", "w", "utf-8-sig") as temp:    
        temp.write('{ "fb" :[ ')
        while (url is not None):	                
            response = urllib.urlopen(url)        
            url = None        
            html = load_html_response(response)        
            url = get_next_url(html)            
            if(url is None):
                temp.write(html)
            else:
                temp.write( html + " ,")           
        temp.write( "]}")
        
		
def load_html_response(response):
    html = response.read()    
    return html

def get_next_url(html):
    url = None
    data = json.loads(html)    
    paging = data.get('paging')    
    if(paging is not None):
        next = paging.get('next')
        if(next is not None):
            url = next    
    return url

					

def main():
    #sent_file = open(sys.argv[1])
    #tweet_file = open(sys.argv[2])
    load_data()

if __name__ == '__main__':
    main()