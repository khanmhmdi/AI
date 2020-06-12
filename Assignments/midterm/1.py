def candidate_generator(x, y, m, n):
    candidate_locations = []
    x_is_float = False if x.is_integer() else True
    y_is_float = False if y.is_integer() else True
    
    
    if x_is_float or y_is_float:
        
        ceil_x, ceil_y  = ceil(x), ceil(y)
        floor_x, floor_y = floor(x), floor(y)
        
        if x_is_float and y_is_float:    
            if ceil_x < n and ceil_y < m:
                candidate_locations.append((floor_x, floor_y))
                candidate_locations.append((floor_x, ceil_y))
                candidate_locations.append((ceil_x, floor_y))
                candidate_locations.append((ceil_x, ceil_y))
            elif ceil_x < n and ceil_y >= m:
                candidate_locations.append((floor_x, floor_y))
                candidate_locations.append((ceil_x, floor_y))
            elif ceil_x >= n and ceil_y < m:
                candidate_locations.append((floor_x, floor_y))
                candidate_locations.append((floor_x, ceil_y))
            else:
                candidate_locations.append((floor_x, floor_y))
                
        elif x_is_float:
            if ceil_x < n:
                candidate_locations.append((floor_x, int(y)))
                candidate_locations.append((ceil_x, int(y)))
            else:
                candidate_locations.append((floor_x, int(y)))

        elif y_is_float:
            if ceil_y < m:
                candidate_locations.append((int(x), floor_y))
                candidate_locations.append((int(x), ceil_y))
            else:
                candidate_locations.append((int(x), floor_y))
    else:
        candidate_locations.append((int(x), int(y)))

    return candidate_locations

def bfs(pacman_locations, game_map, destinations):
    result = []
    for destination in destinations:
        for pacman_location in pacman_locations:
            distances = []
            x, y = pacman_location
            fringe = seen_before = [(x, y)]
            last = (x, y)
            distance = 0
            counter = 1

            while fringe:
                x, y = fringe.pop(0)
                counter -= 1
                if x + 1 < n and game_map[x+1][y] != 2:
                    if (x + 1, y) not in seen_before:
                        fringe.append((x + 1, y))
                        seen_before.append((x + 1, y))
                        if (x + 1, y) == destination:
                            distances.append(distance + 1)
                            break

                if x - 1 >= -1 and game_map[x-1][y] != 2:
                    if (x - 1, y) not in seen_before:
                        fringe.append((x - 1, y))
                        seen_before.append((x - 1, y))
                        if (x - 1, y) == destination:
                            distances.append(distance + 1)
                            break

                if y + 1 < m and game_map[x][y+1] != 2:
                    if (x, y + 1) not in seen_before:
                        fringe.append((x, y + 1))
                        seen_before.append((x, y + 1))
                        if (x, y + 1) == destination:
                            distances.append(distance + 1)
                            break

                if y - 1 >= -1 and game_map[x][y-1] != 2:
                    if (x, y - 1) not in seen_before:
                        fringe.append((x, y - 1))
                        seen_before.append((x, y - 1))
                        if (x, y - 1) == destination:
                            distances.append(distance + 1)
                            break

                if counter == 0:
                    counter = len(fringe)
                    distance += 1

        longest_distance = max(distances)
        result.append(longest_distance)
    return min(result)
            
m, n = list(map(int, input().split()))
game_map = np.zeros((m, n))
pacman_locations = []

for i in range(m):
    cell_info = input().split()
    for j in range(n):
        game_map[i][j] = cell_info[j]
        if game_map[i][j] == 3:
            pacman_locations.append((i, j))

k = len(pacman_locations)
x_sum, y_sum = 0, 0

if k:
    for i in range(k):
        x, y = pacman_locations[i]
        x_sum += y
        y_sum += x


    integration_point = {
        'x': int(x_sum) / k,
        'y': int(y_sum) / k
    }
    
    x, y = integration_point['x'], integration_point['y']
    candidate_locations = candidate_generator(x, y, m, n)
    minimum_distance = bfs(pacman_locations, game_map, candidate_locations)
    print(minimum_distance)

else:
    print('No pacman found!')

