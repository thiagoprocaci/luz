epsilon = 0.001;
    probability = 0.85;
    N = len(nodes)
    pageranks = {}
    temp = {} 
    indicies = {} # node.id, Integer
    index = 0
    weights = {}
    for node_key in nodes:        
        indicies[node_key] = index
        pageranks[index] = 1.0 / N
        sum = 0
        edge_list = []
        for edge_key in edges:
            if(edges.get(edge_key).source.id == node_key):
                edge_list.append(edges.get(edge_key))
        for edge in edge_list:
            sum = sum + edge.weight
        weights[index] = sum			
        index = index + 1	

    print "Inicializacao"
    print ""
    for i in indicies:
        print nodes.get(i).name  
        print "index: " + str(indicies.get(i)) 
        print "pagerank " + str(pageranks[indicies.get(i)])
        print "sum " + str(weights[indicies.get(i)])
        print ""
		
    while True:
        r = 0.0
        for node_key in nodes:
            s_index = indicies.get(node_key)
            out = nodes.get(node_key).outdegree > 0            
            if (out):
                r += (1.0 - probability) * (pageranks[s_index] / N)
                print "rank " + str(r)
                print nodes.get(node_key).name
                print ""
            else:
                r += ((pageranks[s_index] / N))
        done = True
        for node_key in nodes:
            s_index = indicies.get(node_key)
            temp[s_index] = r
            edge_list = []
            for edge_key in edges:
                if(edges.get(edge_key).dest.id == node_key):
                    edge_list.append(edges.get(edge_key))
            for edge in edge_list:
                neighbor = edge.source
                neigh_index = indicies.get(neighbor.id)                
                weight = edges.get(edge_key).weight / weights[neigh_index]
                temp[s_index] += probability * pageranks[neigh_index] * weight
            if ((temp[s_index] - pageranks[s_index]) / pageranks[s_index] >= epsilon):
                done = False
        pageranks = temp
        temp = None
        if (done):
            break
    for node_key in nodes:
        s_index = indicies.get(node_key)
        node = nodes.get(node_key)
        node.pagerank = pageranks[s_index]
		
		
		
def page_rank(nodes, edges):
    damping_factor = 0.85
    max_iterations = 100
    min_delta = 0.00001
     
    graph_size = len(nodes)    
    min_value = ( 1.0 - damping_factor)/ graph_size     
    pagerank = {}
    
    # itialize the page rank dict with 1/N for all nodes    
    for node_key in nodes:                
        pagerank[node_key] = 1.0 / graph_size
        
    for i in range(max_iterations):
        diff = 0 #total difference compared to last iteraction
        # computes each node PageRank based on inbound links
        for node_key in nodes:
            node = nodes.get(node_key)
            rank = min_value
            for referring_page in find_reffering(node, nodes, edges):
                rank += damping_factor * pagerank[referring_page.id] / len(find_reffering(referring_page, nodes, edges))
                
            diff += abs(pagerank[node.id] - rank)
            pagerank[node.id] = rank
        
        #stop if PageRank has converged
        if diff < min_delta:
            break
    print pagerank
    for node_key in nodes:
        print str(pagerank[node_key])
        node = nodes.get(node_key)
        node.pagerank = pagerank[node_key]
    

def find_reffering(node, nodes, edges):
    node_list = []
    for edge_key in edges:
        if(edges.get(edge_key).source.id == node.id):
            node_list.append(edges.get(edge_key).source)
    return node_list