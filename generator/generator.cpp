#include <vector>
#include <string>
#include <random>
#include <ctime>
#include <iostream>

auto getRandom(int min, int max) {
  std::random_device rd;
  std::mt19937 rng(rd());
  std::uniform_int_distribution<int> uni(min, max);
  return uni(rng);
}

int main(int argc, char *argv[]) { 
  // usage: ./generator <n - number of nodes> [<m - number of edges>]
  int n, m, start, end = -1;
  
  if (1 == argc) {
    return EXIT_FAILURE;
  } else if (2 == argc) {
    n = std::atoi(argv[1]);
    m = getRandom(n, n * (n - 1));
  } else {
    n = std::atoi(argv[1]);
    m = std::atoi(argv[2]);
    if (m > (n - 1) * n) {
      printf("m > (n - 1) * n");
      return 0;
    }
  }

  start = getRandom(0, n - 1);
  end = start;
  while (start == end) {
    end = getRandom(0, n - 1);
  }

  printf("%d\n%d\n%d\n%d\n", n, m, start, end);

  std::vector<int> numberOfEdgesPerNode;
  numberOfEdgesPerNode.reserve(n);
  int amount = m / n;
  for (int k = 0; k < n; ++k)
    numberOfEdgesPerNode.push_back(amount);

  for (int k = 0; k < (m - amount * n); ++k)
    ++numberOfEdgesPerNode[getRandom(0, n - 1)];

  for (int i = 0; i < n; ++i) {
    std::vector<int> vec;
    vec.reserve(n);
    for (int k = 0; k < n; ++k)
      vec.push_back(k);
    std::random_shuffle(vec.begin(), vec.end());
    for (int j = 0; j < numberOfEdgesPerNode[i]; ++j) {
      printf("%d %d\n", i, vec[j]);
    }
  }


  return 0;
}

