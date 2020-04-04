infinity = float('inf')

class Path:
    
    # Class Variable of source and terminal
    s, t = None, None
    
    # Dunder/Magic Functions
    def __init__(self, source, dest, cost):
        # Instance Variables
        self.src = source
        self.dst = dest
        self.cost = cost
        
    def __str__(self):
        return 'src: ' + str(self.src) + ', dest: ' + str(self.dst) + ', cost: ' + str(self.cost)
    
    # Normal Functions
    def is_source(self):
        if self.s == self.src:
            return True
        return False
    
    def is_dest(self):
        if self.t == self.dst:
            return True
        return False
    

def adjacent_matrix(n, m):
    adj_matrix = [[0] * n for _ in range(n)]
    paths, priority_queue = [], []
    
    for i in range(m):
        from_, to_, cost_ = list(map(int, input().split()))
        new_path = Path(from_, to_, cost_)
        paths.append(new_path)
        if new_path.is_source():
            priority_queue.append(new_path)
    
    for path in paths:
        adj_matrix[path.src - 1][path.dst - 1] = path.cost
    return adj_matrix


def dijkastra(n, adj_matrix):
    two_shortests = [[infinity, infinity] for i in range(n)]
    two_shortests[Path.s - 1] = [0, infinity]
    
    visited, parents = [False for i in range(n)], [-1 for i in range(n)]
    
    for i in range(n):
        # find the value and the number of unvisited minimum distance node 
        min_distance_value, min_node_no = infinity, -1
    
        for j in range(n):
            if two_shortests[j][0] < min_distance_value and not visited[j]:
                min_node_no = j
                min_distance_value = two_shortests[j][0]

        visited[min_node_no] = True

        for j in range(n):
            if adj_matrix[min_node_no][j] > 0:
                if min_distance_value + adj_matrix[min_node_no][j] < two_shortests[j][0]:
                    parents[j] = min_node_no
                    two_shortests[j].insert(0, min_distance_value + adj_matrix[min_node_no][j])
                    two_shortests[j].pop()
                elif min_distance_value + adj_matrix[min_node_no][j] < two_shortests[j][1]:
                    two_shortests[j][1] = min_distance_value + adj_matrix[min_node_no][j]

    return two_shortests, parents, visited


def shortest_paths(two_shortests, parents, visited):
    first_shortest, second_shortest = two_shortests[Path.t - 1][0], infinity
    current_node = Path.t - 1

    while current_node != -1:
        r = first_shortest - two_shortests[current_node][0]
        second_candidate = two_shortests[current_node][1] + r

        if second_candidate < second_shortest:
            second_shortest = second_candidate

        current_node = parents[current_node]
    
    return first_shortest, second_shortest



n, m, s, t = list(map(int, input().split())) 
Path.s, Path.t = s, t

adj_matrix = adjacent_matrix(n, m)
two_shortests, parents, visited = dijkastra(n, adj_matrix)
first_shortest, second_shortest = shortest_paths(two_shortests, parents, visited)

# print(first_shortest)
print(second_shortest)
