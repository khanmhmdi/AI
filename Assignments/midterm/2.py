from random import randint as rnd

# m = int(input())
# b = int(input())
# c = int(input())

m, b, c = 6, 4, 2

# offers = list()
offers = [[0, 3000.0, [0, 1, 4]],
 [0, 2000.0, [0, 1, 5]],
 [1, 1000.5, [2, 3]],
 [1, 1525.75, [0, 1, 2, 3, 4, 5]]]

local_maximum = 0
results = [0] * b
state = [0 for i in range(0, b)]

# for _ in range(b):
#     offer_details = list(map(float, input()[: -1].split()))
#     protocol_name = int(offer_details[0])
#     price = offer_details[1]
#     regions = list(map(int, offer_details[2:]))
#     offers.append([protocol_name, price, regions])

def get_state_value(state):
    summation, accepted_regions = 0, [0] * m

    for offer_id in range (b):
        if state[offer_id] == 1:
            income = offers[offer_id][1]
            summation += income
            regions = offers[offer_id][2]
            # print('state:', state)
            # print('income:', income)
            # print('regions:', regions)
            for region in regions:
                if accepted_regions[region]:
                    return -1
                accepted_regions[region] = 1

    return summation

def get_random_neighbor(state):
    random_no = rnd(0, b - 1)
    if state[random_no] == 1:
        state[random_no] = 0
    else:
        state[random_no] = 1
    return state

def local_search(state):
    global results, local_maximum

    for i in range(10):
        get_random_neighbor(state)

        if get_state_value(state) > local_maximum:
            results = state[:]
            local_maximum = get_state_value(state)
            local_search(state)
        else:
            pass


local_search(state)
for i in range(500):
    state = results[:]
    local_search(state)


for index, result in enumerate(results):
    if result == 1:
        print(index, end=' ')

print('#')