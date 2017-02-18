#include <bits/stdc++.h>

using namespace std;

typedef pair<int, int> point;

typedef struct marker {
    int point_id;
    long dist;
} marker;

typedef struct item {
    point loc;
    marker content;
} item;

#define default_marker {-1, -1}
#define B_SIZE 1000

int main(int argc, char *argv[]) {
    int N;

    cin >> N;

    vector<point> graph;

    int max_x = 0;
    int max_y = 0;

    for (int i = 0 ; i < N ; i++) {
        int x, y;
        cin >> x >> y;
        max_x = (x > max_x) ? x : max_x;
        max_y = (y > max_y) ? y : max_y;
        graph.push_back({x, y});
    }

    max_x++;
    max_y++;

    unordered_map<int, set<int> > tree_lookup;
    vector<queue<item> > bfs_queues(N);
    vector<vector<marker> > board(max_x);

    for (int i = 0 ; i < max_x ; i++) {
        for (int j = 0 ; j < max_y ; j++) {
            board[i].push_back(default_marker);
        }
    }

    for (int i = 0 ; i < N ; i++) {
        bfs_queues[i].push({graph[i], {i, 0}});
        set<int> tree;
        tree.insert(i);
        tree_lookup[i] = tree;
    }

    long long mst_weight = 0;
    int current_step_size = 0;

    while (1) { // we'll break later
        bool skippedAll = true;
        for (int i = 0 ; i < N ; i++) {
            if (bfs_queues[i].empty())
                continue;

            item current = bfs_queues[i].front();

            if (current.content.dist > current_step_size)
                continue;

            skippedAll = false;

            bfs_queues[i].pop();
            int x = current.loc.first;
            int y = current.loc.second;

            if (board[x][y].point_id == -1) { // that part of board is empty
                //cout << graph[i].first << ", " << graph[i].second << " claims " << x << ", " << y << '\n';

                board[x][y] = current.content;

                // push neighbors onto queue
                if (x + 1 < max_x) {
                    bfs_queues[i].push({{x+1, y}, {i, current.content.dist + 1}});
                }
                if (x - 1 >= 0) {
                    bfs_queues[i].push({{x-1, y}, {i, current.content.dist + 1}});
                }
                if (y + 1 < max_y) {
                    bfs_queues[i].push({{x, y+1}, {i, current.content.dist + 1}});
                }
                if (y - 1 >= 0) {
                    bfs_queues[i].push({{x, y-1}, {i, current.content.dist + 1}});
                }
            }

            else if (board[x][y].point_id == i) { // already visited here
                continue; // skip!
            }

            else { // collision w/ another tree!
                marker other = board[x][y];
                if (tree_lookup[i].count(other.point_id)) { // other point already on this tree
                    continue; // same as if visited by me
                }

                mst_weight += other.dist + current.content.dist;

                // XXX improvement: we know the two sets are disjoint... idk if
                // that helps
                set<int> merged_set(tree_lookup[i]);
                set<int> to_merge = tree_lookup[other.point_id];

                //for (auto it=merged_set.begin(); it!=merged_set.end(); ++it)
                //    std::cout << ' ' << *it;
                //std::cout << '\n';

                //for (auto it=to_merge.begin(); it!=to_merge.end(); ++it)
                //    std::cout << ' ' << *it;
                //std::cout << '\n';

                merged_set.insert(to_merge.begin(), to_merge.end());

                //for (auto it=merged_set.begin(); it!=merged_set.end(); ++it)
                //    std::cout << ' ' << *it;
                //std::cout << '\n';

                if (merged_set.size() == N) { // the tree spans the graph!
                    cout << mst_weight << '\n';
                    return 0;
                }

                //cout << "Updated mst: " << mst_weight << " containing ";
                //cout << merged_set.size() << " elements\n";

                for (auto it=merged_set.begin(); it!=merged_set.end(); ++it) {
                    tree_lookup[*it] = merged_set;
                }

                for (auto it=merged_set.begin(); it!=merged_set.end(); ++it) {
                    tree_lookup[*it] = merged_set;
                }
            }
        }

        if (skippedAll) {
            current_step_size++;
        }
    }


    return 0;
}
