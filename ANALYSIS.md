# Analysis for Assignment 4

## LCS (Longest Common Subsequence)
- **State**: `dp[i][j]` is the length of the LCS of prefixes `a[0..i-1]` and `b[0..j-1]`.
- **Transition**: If `a[i-1] == b[j-1]`, then `dp[i][j] = dp[i-1][j-1] + 1`. Otherwise, `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`.
- **Base case**: `dp[0][j] = dp[i][0] = 0` for all `i, j`.
- **Reconstruction**: Traverse backwards from `dp[n][m]`. If characters match, include them and move diagonally. Otherwise, move in the direction of the larger value. This yields an optimal sequence.
- **Complexity**: Time and space `O(|a| * |b|)`.

## Merge ordered blocks (Interval DP)
- **State**: `dp[l][r]` is the minimum cost to merge blocks from index `l` to `r` into one block.
- **Transition**: For each split `k` between `l` and `r`, `dp[l][r] = min(dp[l][k] + dp[k+1][r] + sum(l..r))`.
- **Base case**: `dp[i][i] = 0` (a single block needs no merge).
- **Plan reconstruction**: We recursively output the child merges first (post-order) and then the parent merge. This ensures the chronological order is valid.
- **Complexity**: Time `O(n^3)` and space `O(n^2)`.

## Tree Independent Set
- **State**: `take[u]` is the maximum weight of a valid set in the subtree of `u` that includes `u`. `skip[u]` is the maximum weight when `u` is excluded.
- **Transition**: `take[u] = weight[u] + sum(skip[v])` for children `v`. `skip[u] = sum(max(take[v], skip[v]))`.
- **Reconstruction**: Traverse from root. If a parent is taken, its children cannot be taken. Otherwise, choose the better option for each child.
- **Complexity**: Time and space `O(n)`.

## String Search (Naive, KMP, Rabin-Karp)
- **Naive**: Checks every possible start and compares characters. Time `O(n*m)`.
- **KMP**: Uses a prefix function `pi[i]` to skip unnecessary comparisons. Time `O(n+m)`. Prefix fallback after a full match allows overlapping matches.
- **Rabin-Karp**: Uses rolling hash with base 31 and mod 101. Hashes of windows are updated in `O(1)`; equal hashes are verified character by character. Time `O(n+m+cm)` where `c` is the number of hash collisions.

## Suffix Array and Kasai LCP
- **Suffix Array**: Built using prefix doubling and comparison sort. Time `O(n log^2 n)`. Space `O(n)`.
- **Kasai LCP**: Computes the longest common prefix between adjacent suffixes. Time `O(n)`. The maximum value in the LCP array gives the length of the longest repeated substring.
- **Output**: The `repeated` string is extracted from the suffix array at the index of the maximum LCP.

## Aho-Corasick
- **Automaton**: Builds a trie of patterns with failure links (suffix links) computed via BFS.
- **Matching**: For each text, traverse the automaton and propagate counts along failure links (reverse BFS order) to account for suffix patterns.
- **Complexity**: Construction `O(L + 95S)`. Query per text `O(|text| + S + p)` with reverse-failure propagation. Duplicate patterns are handled as separate dictionary entries with identical counts.

## Sources and AI Usage
- The algorithms were implemented based on standard textbook descriptions (CLRS, cp-algorithms).
- AI assistance was used to generate code snippets and verify correctness. All code was tested against the provided public tests and passed.