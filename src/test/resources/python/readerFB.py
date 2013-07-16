import sys
import urllib
import json


def load_data():
    token = 'CAACEdEose0cBAHOSmmveFOyE9Gz3p5wQA5YSZBG2ZBSS5ZC77ZAljsVMjfDH38TXWFnyZAZA8DnaU8iRfStquIWVAJNVldrupLA17folJOGC9u5LI0BSdyzPAMWb6aqRrKT7SCQQtIi2TN8jUp5EZAiA9U9ntzZAw04mdt8dZAmMSfwZDZD'
    url = "https://graph.facebook.com/2204806663/feed?&access_token=" + token
    #url = "https://graph.facebook.com/2204806663?fields=members&access_token=" + token
    print '{ "fb" :[ '
    while (url is not None):	                
        response = urllib.urlopen(url)        
        url = None        
        html = load_html_response(response)        
        url = get_next_url(html)
        if(url is None):
            print html            
        else:
            print html + " ,"		
    print "]}"    
		
					
def load_html_response(response):
    #ler a resposta como utf-8
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