import sys
import json

class Node:
    id, name, indegree, outdegree, pagerank, likes = None, None, None, None, None, None
 
    def __init__(self, id, name):     
        self.id = id
        self.name = name
        self.indegree = 0
        self.outdegree = 0
        self.likes = 0
		
    def increase_indegree(self):
        self.indegree = self.indegree + 1

    def increase_outdegree(self):
        self.outdegree = self.outdegree + 1

class Edge:
    id, source, dest, weight = None, None, None, None

    def __init__(self, source, dest):
        self.source = source
        self.dest = dest
        self.id = edge_id_generator(source, dest)
        self.weight = 1

    def increase_weight(self):
        self.weight =  self.weight + 1
		
#metodo auxiliar para gerar os ids dos edges
def edge_id_generator(source, dest):
    id = str(source.id) + "_" + str(dest.id)
    return id
		
#metodo auxiliar para colocar os nodes em um dict
def load_node(nodes, id, name):
    if(nodes.get(id) is None):
        node = Node(id, name)
        nodes[id] = node        
    return nodes

# metodo auxiliar para colocar os edges em um dict
def load_edge(edges, source, dest):
    if(source.id != dest.id):	
        id = edge_id_generator(source, dest)
        if(edges.get(id) is None):
            edge = Edge(source, dest)
            edges[id] = edge	
        else:
            edges.get(id).increase_weight()
    return edges

#carrega os nodes (usuarios) em uma estrutura de dados dado um string json
def load_nodes_edges(json_data): 
    thread_count = 0
    message_count = 0
    nodes = {}
    edges = {}	
    fb_list = json_data.get('fb')
    for fb_item in fb_list:
        item_data = fb_item.get('data')
        for data in item_data:		                
            thread_count = thread_count + 1
            message_count = message_count + 1
            nodes = load_node(nodes, data.get('from').get('id'), data.get('from').get('name'))
            source = nodes.get(data.get('from').get('id'))
            if((data.get('likes') is not None) and (data.get('likes').get('count') is not None)):
                source.likes = source.likes + int(data.get('likes').get('count'))
            if((data.get('comments') is not None) and (data.get('comments').get('data') is not None)):
                comments_data = data.get('comments').get('data')                
                for comment in comments_data:                    
                    message_count = message_count + 1
                    nodes = load_node(nodes, comment.get('from').get('id'), comment.get('from').get('name'))  
                    dest = nodes.get(comment.get('from').get('id'))
                    edges = load_edge(edges, source, dest)		
                    if(comment.get('like_count') is not None):
                        dest.likes = dest.likes + int(comment.get('like_count'))
    #print "threads " + str(thread_count)
    #print "messages " + str(message_count)
    return nodes, edges            

#imprime no padrao gml o grafo
def print_gml(nodes, edges):
    print "graph"
    print "["
    print "    directed 0"
    for key in nodes:
        print "    node"
        print "    ["
        print "      id " + key
        #print '      label "' + nodes.get(key).name + '"'
        print "    ]"
    for key in edges:
        print "    edge"
        print "    ["
        print "      source " + edges.get(key).source.id
        print "      target " + edges.get(key).dest.id
        print "      weight " + str(edges.get(key).weight)
        print "    ]"
    print "]"	

#carrega grafo contido em um arquivo	
def load_graph(file_path):
    fb_file = open(file_path)    
    text = fb_file.read()
    #utf8_text = text.encode('utf-8')
    json_data = json.loads(text)
    nodes, edges = load_nodes_edges(json_data)
    return nodes, edges  

#calcula grau dos nodes
def calc_degree(nodes, edges):
    for key_node in nodes:
        node = nodes.get(key_node)
        for key_edge in edges:
            edge = edges.get(key_edge)
            if(node.id == edge.source.id):
                node.increase_outdegree()
            if(node.id == edge.dest.id):
                node.increase_indegree()

#algoritmo para agrupar as frequencias dos graus
#retorna dict com key=degree value=freq
def count_degree(nodes):
    indegrees = {}
    outdegrees = {}
    for key_node in nodes:
        node = nodes.get(key_node)
        if(indegrees.get(node.indegree) is None):
            indegrees[node.indegree] = 1
        else:
            indegrees[node.indegree] = indegrees[node.indegree] + 1		
        if(outdegrees.get(node.outdegree) is None):
            outdegrees[node.outdegree] = 1
        else:
            outdegrees[node.outdegree] = outdegrees[node.outdegree] + 1
    return indegrees, outdegrees


#transforma grafo em um dict (matriz de adjacencia)
def transform_to_dict(nodes, edges):
    graph = {}
    for key_node in nodes:
        graph[key_node] = []
    for key_edge in edges:
        edge = edges.get(key_edge)
        graph[edge.source.id].append(edge.dest.id)
    return graph
	
 
def count_in_out_nodes(nodes):
    #usuario so faz perguntas   
    count_out = 0
    #usuario que so responde
    count_in = 0
    for key in nodes:
        node = nodes.get(key)
        if(node.indegree == 0 and node.outdegree > 0):
            count_out = count_out + 1
        if(node.outdegree == 0 and node.indegree > 0):
            count_in = count_in + 1
    return count_in, count_out
	
	
#algoritmo para identificar os SCC - tarjan		
#fazer algoritmo para carregar todos os membros
#fazer algoritmo para calcular o z-score
#implementar o page rank
#remover os prints

def main():
    file_path = sys.argv[1]
    nodes, edges = load_graph(file_path)
    calc_degree(nodes, edges)
    indegrees, outdegrees = count_degree(nodes)
    print "in **************************"
    for key in indegrees:
        print str(key) + " ; " + str(indegrees.get(key))
    print "out **************************"
    for key in outdegrees:
        print str(key) + " ; " + str(outdegrees.get(key))
    #count_in, count_out = count_in_out_nodes(nodes)
    #print "usuario que so pergunta: " + str(count_out)
    #print "usuario que so responde: " + str(count_in)
    #print_gml(nodes, edges)
   
    #for node_key in nodes:
     #   print nodes.get(node_key).name + " " + str(nodes.get(node_key).likes)
   

if __name__ == '__main__':
    main()