import random

# create a list of vertices
def vertices_list(vertices_no):
    vertices = {}
    for i in range(vertices_no):
        vertices.update({i + 1 : []})
    return vertices


# count the number of edges that goes out of the partition
def evaluation(current_state, vertices):
    cost = 0
    for partition_vertex in current_state:
        if len(vertices[partition_vertex]) > 1:
            for vertex in vertices[partition_vertex]:
                if vertex not in current_state:
                    cost += 1
        elif len(vertices[partition_vertex]) == 1:
            if vertices[partition_vertex][0] not in current_state:
                cost += 1
    return cost


# create a complementary of given list from the vertices list
def complementary_list(initial_state, vertices):
    complement = []
    for vertex in vertices:
        if vertex not in initial_state:
            complement.append(vertex)
    return complement
    

# create a list of neighbors of given state
def neighbors_list(initial_state, complement):
    neighbors = []
    for i in range(len(initial_state)):
        for neigh in complement:
            neighbors.append(initial_state[0:i] + [neigh] + initial_state[i + 1:])
    return neighbors

# implement hill climbing method
def hill_climb(initial_state, vertices):
    min_value = evaluation(initial_state, vertices)
    current_state = initial_state
    
    while True:
        flag = True
        complement = complementary_list(current_state, vertices)
        neighbors = neighbors_list(current_state, complement)
        for neighbor in neighbors:
            if evaluation(neighbor, vertices) < min_value:
                min_value = evaluation(neighbor, vertices)
                current_state = neighbor
                flag = False
        if flag:
            break
            
    return min_value, current_state, complementary_list(current_state, vertices)

# input 
vertices_no = int(input())
edges_no = int(input())            
vertices = vertices_list(vertices_no)
vertice_list = [i + 1 for i in range(vertices_no)]
initial_state = random.sample(vertice_list, vertices_no // 2)

    
for i in range(edges_no):
    a, b = list(map(int, input().split()))
    vertices[a].append(b)
    vertices[b].append(a)

cost, state, complement = hill_climb(initial_state, vertices)
print(cost)
print(" ".join(str(s) for s in sorted(state)))
print(" ".join(str(c) for c in sorted(complement)))
