public class ArrayHeap {
	Event[] A;
	int n;

	public ArrayHeap(int capacity) {
		A = new Event[capacity];
		n = 0;
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public Event getMin() {
		return A[1];
	}

	public Event[] getArray(){
		return A;
	}

	public Event getObject(int i){
		return A[i];
	}

	public void insert(Event k) {
		A[n+1] = k;
		n++;
		int cIndex = n;
		int pIndex = parentIndex(n);
		//while child is not root and child is smaller than parent
		while (pIndex >= 1 && A[cIndex].getDateTime().getTimeInMillis() < A[pIndex].getDateTime().getTimeInMillis()){
			swap(cIndex,pIndex);
			cIndex = pIndex;
			pIndex = parentIndex(cIndex);
		}
	}

	public Event extractMin() {
		swap(1, n);
		n--;
		int pIndex = 1;
		int lCIndex;
		int rCIndex;
		while (hasSmallerChild(pIndex)) {
			rCIndex = rightChildIndex(pIndex);
			lCIndex = leftChildIndex(pIndex);
			// No right child, then swap parent with left child
			if (rCIndex > n) {
				swap(pIndex, lCIndex);
				pIndex = lCIndex;
			} else { // Two children, swap with the smaller child
				if (A[lCIndex].getDateTime().getTimeInMillis() < A[rCIndex].getDateTime().getTimeInMillis()) {
					swap(pIndex, lCIndex);
					pIndex = lCIndex;
				} else {
					swap(pIndex, rCIndex);
					pIndex = rCIndex;
				}
			}
		}
		return A[n + 1];
	}

	private int parentIndex(int cIndex) {
		return cIndex/2;
	}

	private int leftChildIndex(int pIndex) {
		return pIndex * 2;
	}

	private int rightChildIndex(int pIndex) {
		return (pIndex * 2) + 1;
	}

	private void swap(int pIndex, int cIndex) {
		Event temp = A[cIndex];
		A[cIndex] = A[pIndex];
		A[pIndex] = temp;
	}

	private boolean hasSmallerChild(int pIndex) {
		int rCIndex = rightChildIndex(pIndex);
		int lCIndex = leftChildIndex(pIndex);
		if (rCIndex <= n && A[rCIndex].getDateTime().getTimeInMillis() < A[pIndex].getDateTime().getTimeInMillis())
			return true;
		else if (lCIndex <= n && A[lCIndex].getDateTime().getTimeInMillis() < A[pIndex].getDateTime().getTimeInMillis())
			return true;
		else
			return false;
	}
}
