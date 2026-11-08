import java.util.*;

public final class Algorithms {

    static Map<String,Object> lcs(Map<String,Object> input) {
        String a = (String) input.get("a");
        String b = (String) input.get("b");
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                sb.append(a.charAt(i - 1));
                i--; j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        Map<String,Object> res = new HashMap<>();
        res.put("length", dp[n][m]);
        res.put("sequence", sb.reverse().toString());
        return res;
    }

    static Map<String,Object> merge(Map<String,Object> input) {
        List<?> sizesRaw = (List<?>) input.get("sizes");
        int n = sizesRaw.size();
        long[] sizes = new long[n];
        for (int i = 0; i < n; i++) sizes[i] = ((Number) sizesRaw.get(i)).longValue();
        
        if (n <= 1) {
            Map<String,Object> res = new HashMap<>();
            res.put("cost", 0L);
            res.put("plan", new ArrayList<>());
            return res;
        }
        
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + sizes[i];
        
        long[][] dp = new long[n][n];
        int[][] split = new int[n][n];
        
        for (int len = 2; len <= n; len++) {
            for (int l = 0; l + len - 1 < n; l++) {
                int r = l + len - 1;
                dp[l][r] = Long.MAX_VALUE;
                for (int k = l; k < r; k++) {
                    long cost = dp[l][k] + dp[k + 1][r] + (prefix[r + 1] - prefix[l]);
                    if (cost < dp[l][r]) {
                        dp[l][r] = cost;
                        split[l][r] = k;
                    }
                }
            }
        }
        
        List<List<Integer>> plan = new ArrayList<>();
        buildPlan(0, n - 1, split, plan);
        
        Map<String,Object> res = new HashMap<>();
        res.put("cost", dp[0][n - 1]);
        res.put("plan", plan);
        return res;
    }
    
    private static void buildPlan(int l, int r, int[][] split, List<List<Integer>> plan) {
        if (l >= r) return;
        int k = split[l][r];
        buildPlan(l, k, split, plan);
        buildPlan(k + 1, r, split, plan);
        plan.add(Arrays.asList(l, k, r));
    }

    static Map<String,Object> tree(Map<String,Object> input) {
        int n = ((Number) input.get("n")).intValue();
        if (n == 0) {
            Map<String,Object> res = new HashMap<>();
            res.put("weight", 0L);
            res.put("selected", new ArrayList<>());
            return res;
        }
        
        List<?> weightsRaw = (List<?>) input.get("weights");
        long[] weights = new long[n];
        for (int i = 0; i < n; i++) weights[i] = ((Number) weightsRaw.get(i)).longValue();
        
        List<?> edgesRaw = (List<?>) input.get("edges");
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Object eObj : edgesRaw) {
            List<?> e = (List<?>) eObj;
            int u = ((Number) e.get(0)).intValue();
            int v = ((Number) e.get(1)).intValue();
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] order = new int[n];
        int cnt = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        boolean[] visited = new boolean[n];
        visited[0] = true;
        while (!stack.isEmpty()) {
            int u = stack.pop();
            order[cnt++] = u;
            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    parent[v] = u;
                    stack.push(v);
                }
            }
        }
        
        long[] take = new long[n];
        long[] skip = new long[n];
        for (int i = n - 1; i >= 0; i--) {
            int u = order[i];
            take[u] = weights[u];
            skip[u] = 0;
            for (int v : adj.get(u)) {
                if (v != parent[u]) {
                    take[u] += skip[v];
                    skip[u] += Math.max(take[v], skip[v]);
                }
            }
        }
        
        List<Integer> selected = new ArrayList<>();
        boolean[] chosen = new boolean[n];
        for (int i = 0; i < n; i++) {
            int u = order[i];
            if (parent[u] == -1) {
                if (take[u] >= skip[u]) chosen[u] = true;
            } else {
                int p = parent[u];
                if (chosen[p]) {
                    chosen[u] = false;
                } else {
                    if (take[u] >= skip[u]) chosen[u] = true;
                }
            }
            if (chosen[u]) selected.add(u);
        }
        
        long total = 0;
        for (int u : selected) total += weights[u];
        
        Map<String,Object> res = new HashMap<>();
        res.put("weight", total);
        res.put("selected", selected);
        return res;
    }

    static Map<String,Object> naive(Map<String,Object> input) {
        String text = (String) input.get("text");
        String pattern = (String) input.get("pattern");
        List<Integer> positions = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) positions.add(i);
        } else if (m <= n) {
            for (int i = 0; i <= n - m; i++) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) positions.add(i);
            }
        }
        Map<String,Object> res = new HashMap<>();
        res.put("positions", positions);
        return res;
    }

    static Map<String,Object> kmp(Map<String,Object> input) {
        String text = (String) input.get("text");
        String pattern = (String) input.get("pattern");
        List<Integer> positions = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) positions.add(i);
        } else if (m <= n) {
            int[] pi = new int[m];
            for (int i = 1; i < m; i++) {
                int j = pi[i - 1];
                while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) j = pi[j - 1];
                if (pattern.charAt(i) == pattern.charAt(j)) j++;
                pi[i] = j;
            }
            int j = 0;
            for (int i = 0; i < n; i++) {
                while (j > 0 && text.charAt(i) != pattern.charAt(j)) j = pi[j - 1];
                if (text.charAt(i) == pattern.charAt(j)) j++;
                if (j == m) {
                    positions.add(i - m + 1);
                    j = pi[j - 1];
                }
            }
        }
        Map<String,Object> res = new HashMap<>();
        res.put("positions", positions);
        return res;
    }

    static Map<String,Object> rk(Map<String,Object> input) {
        String text = (String) input.get("text");
        String pattern = (String) input.get("pattern");
        List<Integer> positions = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) positions.add(i);
            Map<String,Object> res = new HashMap<>();
            res.put("positions", positions);
            return res;
        }
        if (m > n) {
            Map<String,Object> res = new HashMap<>();
            res.put("positions", positions);
            return res;
        }
        int base = 31, mod = 101;
        long patHash = 0, txtHash = 0, h = 1;
        for (int i = 0; i < m - 1; i++) h = (h * base) % mod;
        for (int i = 0; i < m; i++) {
            patHash = (patHash * base + pattern.charAt(i)) % mod;
            txtHash = (txtHash * base + text.charAt(i)) % mod;
        }
        for (int i = 0; i <= n - m; i++) {
            if (patHash == txtHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) positions.add(i);
            }
            if (i < n - m) {
                txtHash = (txtHash - text.charAt(i) * h % mod + mod) % mod;
                txtHash = (txtHash * base + text.charAt(i + m)) % mod;
            }
        }
        Map<String,Object> res = new HashMap<>();
        res.put("positions", positions);
        return res;
    }

    static Map<String,Object> suffix(Map<String,Object> input) {
        String s = (String) input.get("text");
        int n = s.length();
        if (n == 0) {
            Map<String,Object> res = new HashMap<>();
            res.put("sa", new ArrayList<>());
            res.put("lcp", new ArrayList<>());
            res.put("repeated", "");
            return res;
        }
        
        Integer[] sa = new Integer[n];
        int[] rank = new int[n];
        int[] tmp = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = s.charAt(i);
        }
        
        for (int k = 1; k < n; k *= 2) {
            final int K = k;
            final int[] r = rank;
            Arrays.sort(sa, (x, y) -> {
                if (r[x] != r[y]) return Integer.compare(r[x], r[y]);
                int rx = x + K < n ? r[x + K] : -1;
                int ry = y + K < n ? r[y + K] : -1;
                return Integer.compare(rx, ry);
            });
            tmp[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                int x = sa[i - 1], y = sa[i];
                int rx = x + K < n ? r[x + K] : -1;
                int ry = y + K < n ? r[y + K] : -1;
                tmp[y] = tmp[x] + (r[x] != r[y] || rx != ry ? 1 : 0);
            }
            System.arraycopy(tmp, 0, rank, 0, n);
        }
        
        int[] lcp = new int[n];
        int[] inv = new int[n];
        for (int i = 0; i < n; i++) inv[sa[i]] = i;
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (inv[i] > 0) {
                int j = sa[inv[i] - 1];
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) h++;
                lcp[inv[i]] = h;
                if (h > 0) h--;
            } else {
                lcp[inv[i]] = 0;
                h = 0;
            }
        }
        
        int maxLcp = 0, maxIdx = -1;
        for (int i = 1; i < n; i++) {
            if (lcp[i] > maxLcp) {
                maxLcp = lcp[i];
                maxIdx = i;
            }
        }
        String repeated = maxIdx != -1 ? s.substring(sa[maxIdx], sa[maxIdx] + maxLcp) : "";
        
        List<Integer> saList = new ArrayList<>();
        for (int x : sa) saList.add(x);
        List<Integer> lcpList = new ArrayList<>();
        for (int x : lcp) lcpList.add(x);
        
        Map<String,Object> res = new HashMap<>();
        res.put("sa", saList);
        res.put("lcp", lcpList);
        res.put("repeated", repeated);
        return res;
    }

    static Map<String,Object> aho(Map<String,Object> input) {
        List<?> patternsRaw = (List<?>) input.get("patterns");
        List<?> textsRaw = (List<?>) input.get("texts");
        int p = patternsRaw.size();
        List<String> patterns = new ArrayList<>();
        for (Object o : patternsRaw) patterns.add((String) o);
        
        int[][] next = new int[100005][95];
        for (int[] row : next) Arrays.fill(row, -1);
        int[] fail = new int[100005];
        int[] out = new int[100005];
        int sz = 1;
        fail[0] = -1;
        
        for (int i = 0; i < p; i++) {
            String pat = patterns.get(i);
            int u = 0;
            for (char c : pat.toCharArray()) {
                int idx = c - 32;
                if (next[u][idx] == -1) {
                    next[u][idx] = sz++;
                }
                u = next[u][idx];
            }
        }
        
        Deque<Integer> q = new ArrayDeque<>();
        for (int c = 0; c < 95; c++) {
            if (next[0][c] != -1) {
                fail[next[0][c]] = 0;
                q.add(next[0][c]);
            } else {
                next[0][c] = 0;
            }
        }
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int c = 0; c < 95; c++) {
                if (next[u][c] != -1) {
                    int v = next[u][c];
                    fail[v] = next[fail[u]][c];
                    q.add(v);
                } else {
                    next[u][c] = next[fail[u]][c];
                }
            }
        }
        
        List<List<Integer>> counts = new ArrayList<>();
        for (Object tObj : textsRaw) {
            String text = (String) tObj;
            int[] cnt = new int[sz];
            int u = 0;
            for (char ch : text.toCharArray()) {
                u = next[u][ch - 32];
                cnt[u]++;
            }
            List<Integer>[] revFail = new ArrayList[sz];
            for (int i = 0; i < sz; i++) revFail[i] = new ArrayList<>();
            for (int i = 1; i < sz; i++) {
                if (fail[i] >= 0) revFail[fail[i]].add(i);
            }
            Deque<Integer> bfs = new ArrayDeque<>();
            int[] indeg = new int[sz];
            for (int i = 0; i < sz; i++) {
                for (int v : revFail[i]) indeg[v]++;
            }
            for (int i = 0; i < sz; i++) if (indeg[i] == 0) bfs.add(i);
            while (!bfs.isEmpty()) {
                int v = bfs.poll();
                for (int child : revFail[v]) {
                    cnt[v] += cnt[child];
                    indeg[child]--;
                    if (indeg[child] == 0) bfs.add(child);
                }
            }
            List<Integer> row = new ArrayList<>();
            for (int i = 0; i < p; i++) {
                String pat = patterns.get(i);
                int node = 0;
                for (char c : pat.toCharArray()) node = next[node][c - 32];
                row.add(cnt[node]);
            }
            counts.add(row);
        }
        
        Map<String,Object> res = new HashMap<>();
        res.put("counts", counts);
        return res;
    }
}
