
function swap(elements, posA, posB) {
  var swap = elements[posA];
  elements[posA] = elements[posB];
  elements[posB] = swap;
}
function headAdjuct(elements, pos, len) {
  var swap = elements[pos];
  var child = pos * 2 + 1;
  while (child < len) {
    if (child + 1 < len && elements[child] < elements[child + 1]) {
      child += 1;
    }
    if (elements[pos] < elements[child]) {
      elements[pos] = elements[child];
      pos = child;
      child = pos * 2 + 1;
    } else {
      break;
    }

    elements[pos] = swap;
  }
}

function buildHeap(elements) {
  let halfLen = Math.floor(elements.length / 2)
  for (var i = halfLen; i >= 0; i--) {
    headAdjuct(elements, i, elements.length);
  }
}
function sort(elements) {
  buildHeap(elements);
  for (var i = elements.length - 1; i > 0; i--) {
    swap(elements, 0, i);
    headAdjuct(elements, 0, i);
  }
}

var elements = [3, 1, 5, 7, 2, 4, 9, 6, 10, 8, 33, 2, 21, 2, 15, 22, 77, 11, 0, -1, 23345, 12];
sort(elements);
