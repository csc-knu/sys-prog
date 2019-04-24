#include <iostream>
#include <vector>
#include <tuple>
#include <utility>

using namespace std;

typedef long long llong;
typedef long double ldouble;

bool func(llong m, llong k, const vector<tuple<ldouble, llong, llong>>& s) {
	// cout << "func(" << m << ") called.\n";
	priority_queue<tuple<ldouble, llong, llong>> pq;

	for (const auto si: s)
		pq.push(si);

	ldouble c;
	llong a, b;
	for (llong t = 0; t < k; ++t) {
		tie(c, a, b) = pq.top();
	}
	// cout << "\tpq.top = " << c << ", " << a << ", " << b << "\n";

	if (a - b * t < 0)
		return false;
	
	pq.pop();
	a += m;
	c = -a / b;
	pq.push(make_tuple(c, a, b));

	while (!pq.empty()) {
		tie(c, a, b) = pq.top();
		pq.pop();
		if (a - b * (k - 1) < 0)
			return false;
	}

	return true;
}

llong bin_find(llong l, llong r, llong k, const vector<tuple<ldouble, llong, llong>>& s) {
	// cout << "bin_find(" << l << ", " << r << ") called.\n";

	if (l == r)
		return l;

	llong m = (l + r) >> 1;

	if (func(m, k, s))
		return bin_find(l, m, k, s);
	else
		return bin_find(m + 1, r, k, s);
};

int main() {
	llong n, k; cin >> n >> k;

	vector<llong> a(n), b(n);
	for (llong i = 0; i < n; ++i)
		cin >> a[i];
	for (llong i = 0; i < n; ++i)
		cin >> b[i];

	vector<tuple<ldouble, llong, llong>> s(n);
	for (llong i = 0; i < n; ++i)
		s[i] = make_tuple(-a[i] / b[i], a[i], b[i]);

	llong ans = bin_find(0, 2e12, k, s);

	if (ans == 2e12)
		cout << -1 << "\n";
	else
	  cout << ans << "\n";

	return 0;
}