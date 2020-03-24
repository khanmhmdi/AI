import random
import math

def vertices_dict(vertices_no):
    vertices = {}
    for i in range(vertices_no):
        vertices.update({i + 1 : []})
    return vertices


def complementary_list(initial_state, vertices):
    complement = []
    for i in vertices:
        if i not in initial_state:
            complement.append(i)
    return complement


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


def random_neighbors(initial_state, complement):
    neighbors = []
    for i in range(len(initial_state)):
        for neigh in complement:
            neighbors.append(initial_state[0:i] + [neigh] + initial_state[i + 1:])
    return random.sample(neighbors, 1)[0]


def random_schedule_creator():
    first_t = 20
    schedule = []
    schedule.append(first_t)
    temperature, n_step = first_t, 2
    while True:
        temperature -= (1/math.log(n_step))
        if temperature > 0:
            schedule.append(temperature)
        else:
            schedule.append(0)
            break
        n_step += 1
    return schedule


def random_state_generator(vertices_no):
    states = [i + 1 for i in range(vertices_no) ]
    return random.sample(states, vertices_no // 2)
    

def print_results(cost, state, complement):
    print(cost)
    print(" ".join(str(s) for s in sorted(state)))
    print(" ".join(str(c) for c in sorted(complement)))
    
    
def simulated_annealing(vertices, vertices_dict):
    index, schedule = 0, random_schedule_creator()
    current = random_state_generator(len(vertices))
    while True:
        T = schedule[index]
        if T == 0:
            complement_1 = complementary_list(current, vertices)
            cost = evaluation(current, vertices_dict)
            return cost, current, complement_1
        complement = complementary_list(current, vertices)
        nextState = random_neighbors(current, complement)
        
        delta_E = evaluation(nextState, vertices_dict) - evaluation(current, vertices_dict)
        if delta_E > 0:
            currnet = nextState
        else:
            p = math.exp(delta_E / T)
            current = random.choices(
                population = [nextState, current],
                weights = [p, 1 - p]
            )[0]
        
        index += 1
        
    
vertices_no = int(input())
edges_no = int(input())

vertices = vertices_dict(vertices_no)
vertices_list = [i + 1 for i in range(vertices_no)]

for i in range(edges_no):
    a, b = list(map(int, input().split()))
    vertices[a].append(b)
    vertices[b].append(a)

cost, state, complement =  simulated_annealing(vertices_list, vertices)
print_results(cost, state, complement)
