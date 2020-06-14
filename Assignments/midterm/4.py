def get_path(q1, q2, path):
    path.append(q1)
    visited[q1] = True
    if q1 != q2:
        for edge in edges[q1]:
            if not visited[edge]:
                get_path(edge, q2, path)
    else:
        paths.append(path[:])
    path.pop()
    visited[q1] = False
    
def get_list_int():
    return list(map(int, input().split()))


def get_edges(m):
    edges, all_edges = dict(), dict()
    for _ in range(m):
        first, second = get_list_int()
        if first not in edges:
            edges[first] = list()
        if first not in all_edges:
            all_edges[first] = list()
        if second not in all_edges:
            all_edges[second] = list()
        edges[first].append(second)
        all_edges[first].append(second)
        all_edges[second].append(first)
    return edges, all_edges


def get_evidence(z):
    evidences = set()
    for _ in range(z):
        evidence = int(input())
        evidences.add(evidence)
    return evidences


def get_query():
    queries = get_list_int()
    q1, q2 = queries[0], queries[1]
    return q1, q2


# input
n, m, z = get_list_int()
edges, all_edges = get_edges(m)
paths, visited = list(), [False] * (n + 1)
q1, q2 = get_query()

#output
path = []
get_path(q1, q2, path)
get_result()