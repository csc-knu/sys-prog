#include <bits/stdc++.h>

using namespace std;

int main() {
	int n, q; cin >> n >> q;

	vector<tuple<int, char, int>> lri;
	for (int i = 0, l, r; i < q; ++i) {
		cin >> l >> r;
		lri.push_back(make_tuple(l, 'l', i));
		lri.push_back(make_tuple(r + 1, 'r', i));
	}

	sort(begin(lri), end(lri));

	int total = 0, cc = 0, cp = 0, ci = 0;
	set<int> cs;
	map<int, int> mic;
	map<pair<int, int>, int> mpic;

	while (cp <= n) {
		++cp;

		while ((ci < (q << 1)) & (get<0>(lri[ci]) == cp)) {
			if (get<1>(lri[ci]) == "l") {
				cs.insert(get<2>(lri[ci]));
				++cc;
			} else {
				cs.erase(get<2>(lri[ci]));
				--cc;
			}

			++ci;
		}

		if (cc > 0)
			++total;

		if (cc == 1) {
			for (int c: cs) {
				if (mic.find(c) == mic.end())
					mic[c] = 0;
				++mic[c];
			}
		}

		if (cc == 2) {
			vector<int> tmp;
			for (int c: cs)
				tmp.push_back(c);
			if (tmp[0] > tmp[1]) {
				int ttmp = tmp[0];
				tmp[0] = tmp[1];
				tmp[1] = ttmp;
			}
			pair<int, int> pc = make_pair(tmp[0], tmp[1]);
			if (mpic.find(pc) == mpic.end())
				mpic[pc] = 0;
			++mpic[pc];
		}
	}

	int ans = 0;

	for (int i = 0; i < q; ++i) {
		int cur = total;
		if (mic.find(i) != mic.end())
			cur -= mic[i];
		for (int j = i + 1; j < q; ++j) {
			int cur2 = cur;
			if (mic.find(j) != mic.end())
				cur2 -= mic[j];
			pair<int, int> ij = make_pair(i, j);
			if (mpic.find(ij) != mpic.end())
				cur2 -= mpic[ij];
			ans = max(ans, cur2);
		}
	}

	cout << ans;

	return 0;
}