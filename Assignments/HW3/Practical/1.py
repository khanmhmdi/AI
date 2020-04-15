from random import shuffle

n = int(input())
domains = {}
teams_info = {}

def select_unassigned_variable(assignment):
    shuffled_list = list(domains.keys())
    shuffle(shuffled_list)
    for variable in shuffled_list:
        if variable not in assignment:
            return variable

def recursive_backtracking(assignment, domains):
    new_domain = domains.copy()
    if len(assignment) == n:
        return assignment
    var = select_unassigned_variable(assignment)
    for value in new_domain[var]:
        assignment[var] = value
        for i in new_domain:
            if value in new_domain[i]:
                new_domain[i].remove(value)
                
        result = recursive_backtracking(assignment, new_domain.copy())
        if result != False:
            return result
        del assignment[var]
    return False
    

def backtracking_search():
    return recursive_backtracking({}, domains.copy())

def remove_incosistent_values(x_i, x_j):
    if x_i == x_j:
        return False
    removed = False
    for d_i in domains[x_i]:
        counter = 0
        for d_j in domains[x_j]:
            if d_i != d_j:
                if abs(teams_info[x_i] - teams_info[d_i]) <= 2:
                    if abs(teams_info[x_j] - teams_info[d_j]) <= 2:
                        counter += 1
        if counter == 0:
            domains[x_i].remove(d_i)
            removed = True
    return removed

def ac_3():
    queue = [] # all possible list of arcs
    for i in range(1, n + 1):
        for j in range(i + 1, n + 1):
            queue.append((i, j))
            queue.append((j, i))
            
    while queue:
        x_i, x_j = queue.pop()
        if remove_incosistent_values(x_i, x_j):
            for x_k in range(1, n + 1):
                if x_k == x_i or x_k == x_j:
                    continue
                else:
                    queue.append((x_k, x_i))

    


for i in range(2 * n):
    team, power = list(map(int, input().split()))
    teams_info[team] = power

for i in range(1, n + 1):
    domains[i] = [d for d in range(n + 1, 2 * n + 1)]
    
ac_3()
result = backtracking_search()

if result == False:
    print("None")
else:
    for i in list(result):
        b = result[i]
        result[b] = i
    while True:
        input_ = input()
        if input_ == "end":
            break
        else:
            print(result[int(input_)])

