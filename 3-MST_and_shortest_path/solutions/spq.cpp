#include <set>
#include <iostream>
#include <vector>
#include <map>


using namespace std;

#define INF 123123123
#define ii pair<int, int>
#define iii pair<ii, int>

int main()
{
        int n, m, C;
        cin>>n>>m>>C;

        vector <vector<int>> graph(n);
        int cost[n][n];
        int color[n][n];

        int v, u, w, c;
        for (int i = 0; i < m; i++)
        {
            cin>>v>>u>>w>>c;
            v--;
            u--;
            graph[v].push_back(u);
            graph[u].push_back(v);
            cost[v][u] = w;
            cost[u][v] = w;
            color[v][u] = c;
            color[u][v] = c;

        }
        int s, q;
        cin>>s>>q;
        s--;

        vector <bool> passed(n);
        vector <ii> distance(n);
        vector <ii> colorNode(n);
        vector <int> queries(q);
        map <int, int> sols;
        set <iii> sortedNodes; //{dist, curr}
        for (int i = 0; i < n; i++)
        {
            passed[i] = false;
            distance[i] = {INF, INF};
            colorNode[i] = {-1, -1};
            sortedNodes.insert({distance[i], i});
        }

        int t;
        for (int i = 0; i < q; i++)
        {
            cin>>t;
            t--;
            queries[i] = t;
            sols[t] = -1;
        }

        sortedNodes.erase({distance[s], s});
        distance[s] = {0, 0};
        sortedNodes.insert({distance[s], s});

        int solved = 0;

        while (solved < q)
        {
            iii closest = *sortedNodes.begin();
            sortedNodes.erase(closest);
            int currentNode = closest.second;
            //cout<<"hi: "<<currentNode<<endl;
            passed[currentNode] = true;
            if (sols.count(currentNode) == 1)
            {
                sols[currentNode] = distance[currentNode].first;
                solved++;
            }
            for (int neighbor: graph[currentNode])
            {
                if (passed[neighbor])
                {
                    continue;
                }
                int currentCost;
                if (colorNode[currentNode].first != color[currentNode][neighbor])
                {
                   currentCost = distance[currentNode].first;
                }
                else
                {
                    currentCost = distance[currentNode].second;
                }
                //cout<<currentNode<<" "<<neighbor<<" "<<currentCost<<endl;

                ii oldNeighborDistance = distance[neighbor];
                bool change = false;
                if (currentCost + cost[currentNode][neighbor] < distance[neighbor].first)
                {
                    distance[neighbor].second = distance[neighbor].first;
                    distance[neighbor].first = currentCost + cost[currentNode][neighbor];
                    colorNode[neighbor].second = colorNode[neighbor].first;
                    colorNode[neighbor].first = color[currentNode][neighbor];
                    change = true;
                }
                else if(currentCost + cost[currentNode][neighbor] < distance[neighbor].second)
                {
                    distance[neighbor].second = currentCost + cost[currentNode][neighbor];
                    colorNode[neighbor].second = color[s][neighbor];
                    change = true;
                }
                if (change)
                {
                    //cout<<"update: "<<distance[neighbor].first<<" "<<distance[neighbor].second<<" "<<neighbor<<endl;
                    sortedNodes.erase({oldNeighborDistance, neighbor});
                    sortedNodes.insert({distance[neighbor], neighbor});
                }

            }


        }

        for (int query: queries)
        {
            if (sols[query] == INF)
            {
                cout<<"-1"<<endl;
            }
            else
            {
                cout<<sols[query]<<endl;
            }

        }


}
