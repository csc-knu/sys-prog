#include <math.h>
#include <stdlib.h>
#include <stdio.h>

/*
 * 4 4
 * 1  2  3  4
 * 5  6  7  8
 * 9  10 11 12
 * 13 14 15 0
 */

typedef int Cell;
const Cell EMPTY = 0;

typedef enum bool {FALSE, TRUE} bool;
typedef enum Move {LEFT, UP, RIGHT, DOWN} Move;

typedef struct Board {
  int rows, columns;
  Cell** data;

  Cell* data_storage;
} Board;

typedef Board* pBoard;

typedef struct Storage {
  int size, capacity;
  Board* begin, * end;
  pBoard* prev_begin, * prev_end;
} Storage;

typedef struct Queue {
  int size, capacity;
  pBoard * head, * tail;
} Queue;

void AddToStorage(Storage* storage, Board* board, pBoard prev); //
void ReAllocateStorage(Storage* storage);

void SiftUp(Queue* queue, pBoard* pboard);
void SiftDown(Queue* queue, pBoard* pboard);
void ReAllocate(Queue* queue);

void Add(Queue* queue, Board* board, Storage* storage);
void PopHead(Queue* queue);

int Jesus(Board* board);
Board* ProceedMove(const Board* board, Move move);
void AddMoveQueue(Queue* queue, Storage* storage, Board* board); //

Board Read(FILE* file);
void Print(FILE* file, const Board* board);

int main() {
  Board board = Read(stdin);

  Board* move_1 = ProceedMove(&board, LEFT);
  Board* move_2 = ProceedMove(move_1, UP);
  Board* move_3 = ProceedMove(move_2, LEFT);
  Board* move_4 = ProceedMove(move_3, DOWN);

  Print(stdout, move_1);
  Print(stdout, move_2);
  Print(stdout, move_3);
  Print(stdout, move_4);

  return 0;
}

Board* CopyBoard(const Board* board);

Board* ProceedMove(const Board* board, Move move) {
  Board* new_board = CopyBoard(board);
  int x, y;
  x = 0, y = 0;

  bool flag = TRUE;
  for (x = 0; flag && x < new_board->rows; ++x) {
    for (y = 0; flag && y < new_board->columns; ++y) {
      if (new_board->data[x][y] == EMPTY) {
        flag = FALSE;
        break;
      }
    }
    if (flag == FALSE) {
      break;
    }
  }

  switch (move) {
    case UP: {
      if (x == 0) {
        return NULL;
      } else {
        new_board->data[x][y] = new_board->data[x - 1][y];
        new_board->data[x - 1][y] = EMPTY;
      }
      break;
    }
    case DOWN: {
      if (x + 1 == new_board->rows) {
        return NULL;
      } else {
        new_board->data[x][y] = new_board->data[x + 1][y];
        new_board->data[x + 1][y] = EMPTY;
      }
      break;
    }
    case LEFT: {
      if (y == 0) {
        return NULL;
      } else {
        new_board->data[x][y] = new_board->data[x][y - 1];
        new_board->data[x][y - 1] = EMPTY;
      }
      break;
    }
    case RIGHT: {
      if (y + 1 == new_board->columns) {
        return NULL;
      } else {
        new_board->data[x][y] = new_board->data[x][y + 1];
        new_board->data[x][y + 1] = EMPTY;
      }
      break;
    }
  }

  return new_board;
}

void Allocate(Board* board);

Board* CopyBoard(const Board* board) {
  Board* new_board = malloc(sizeof(Board));
  new_board->rows = board->rows;
  new_board->columns = board->columns;

  Allocate(new_board);

  for (int x = 0; x < board->rows; ++x) {
    for (int y = 0; y < board->columns; ++y) {
      new_board->data[x][y] = board->data[x][y];
    }
  }

  return new_board;
}

void AddToStorage(Storage* storage, Board* board, pBoard prev) {
  ReAllocateStorage(storage);
  *storage->end = *board;
  ++storage->end;
  ++storage->size;
  *storage->prev_end = board;
  ++storage->prev_end;
}

Board* Copy(Board* in_begin, Board* in_end, Board* out_begin);
pBoard* pCopy(pBoard* in_begin, pBoard* in_end, pBoard* out_begin);

void ReAllocateStorage(Storage* storage) {
  if (storage->size == storage->capacity || 4 * storage->size < storage->capacity) {
    int new_capacity = storage->size * 2 + 1;

    Board* new_begin = malloc(new_capacity * sizeof(Board));
    Board* new_end = Copy(storage->begin, storage->end, new_begin);
    Board* old_begin = storage->begin;

    pBoard* new_p_begin = malloc(new_capacity * sizeof(pBoard));
    pBoard* new_p_end = pCopy(storage->prev_begin, storage->prev_end, new_p_begin);
    pBoard* old_p_begin = storage->prev_begin;

    storage->capacity = new_capacity;
    storage->begin = new_begin;
    storage->end = new_end;
    storage->prev_begin = new_p_begin;
    storage->prev_end = new_p_end;

    for (pBoard* new_prev = new_p_begin; new_prev != new_p_end; ++new_prev) {
      *new_prev = (pBoard)((long)(*new_prev) + (long)(new_begin) - (long)(old_begin));
    }

    free(old_begin);
    free(old_p_begin);
  }
}

Board* Copy(Board* in_begin, Board* in_end, Board* out_begin) {
  for (; in_begin != in_end; ++in_begin, ++out_begin) {
    *out_begin = *in_begin;
  }
  return out_begin;
}

pBoard* Dad(const Queue* queue, const pBoard* node);
pBoard* LeftSon(const Queue* queue, const pBoard* node);
pBoard* RightSon(const Queue* queue, const pBoard* node);

void Swap(pBoard* a, pBoard* b);

void SiftUp(Queue* queue, pBoard* node) {
  pBoard* dad = Dad(queue, node);
  if (dad != NULL) {
    if (Jesus(*node) < Jesus(*dad)) {
      Swap(node, dad);
      SiftUp(queue, dad);
    }
  }
}

void SiftDown(Queue* queue, pBoard* node) {
  pBoard * left_son = LeftSon(queue, node);
  pBoard * right_son = RightSon(queue, node);

  if (left_son == NULL) {
    return;
  } else if (right_son == NULL) {
    if (Jesus(*node) > Jesus(*left_son)) {
      Swap(node, left_son);
      SiftDown(queue, left_son);
    }
  } else {
    pBoard* best_son = (Jesus(*left_son) < Jesus(*right_son)) ? left_son : right_son;
    if (Jesus(*best_son) < Jesus(*node)) {
      Swap(node, best_son);
      SiftDown(queue, best_son);
    }
  }
}

void Swap(pBoard* a, pBoard* b) {
  pBoard buf = *a;
  *a = *b;
  *b = buf;
}

pBoard* Dad(const Queue* queue, const pBoard* node) {
  pBoard* dad = (pBoard*)(((long)node - (long)(queue->head) - sizeof(pBoard*)) / 2 + (long)(queue->head));
  if (dad >= queue->head && dad < queue->tail) {
    return dad;
  } else {
    return NULL;
  }
}

pBoard* LeftSon(const Queue* queue, const pBoard* node) {
  pBoard* left_son = (pBoard*) (2 * ((long) node) + 1 - ((long) queue->head));
  if (left_son < queue->tail) {
    return left_son;
  } else {
    return NULL;
  }
}

pBoard* RightSon(const Queue* queue, const pBoard* node) {
  pBoard* right_son = (pBoard*) (2 * ((long) node) + 2 - ((long) queue->head));
  if (right_son < queue->tail) {
    return right_son;
  } else {
    return NULL;
  }
}

bool Consist(Storage* storage, const Board* board);

void Add(Queue* queue, Board* board, Storage* storage) {
  if (!Consist(storage, board)) {
    ReAllocate(queue);
    ++queue->size;
    *queue->tail = board;
    SiftUp(queue, queue->tail);
    ++queue->tail;
  }
}

bool Equal(const Board* fst, const Board* snd);

bool Consist(Storage* storage, const Board* board) {
  for (Board* stored = storage->begin; stored != storage->end; ++stored) {
    if (Equal(stored, board) == TRUE) {
      return TRUE;
    }
  }
  return FALSE;
}

bool Equal(const Board* fst, const Board* snd) {
  if (fst->rows != snd->rows || fst->columns != snd->columns) {
    return FALSE;
  }

  for (int x = 0; x < fst->rows; ++x) {
    for (int y = 0; y < fst->columns; ++y) {
      if (fst->data[x][y] != snd->data[x][y]) {
        return FALSE;
      }
    }
  }

  return TRUE;
}

void PopHead(Queue* queue) {
  --queue->size;
  *queue->tail = *queue->head;
  --queue->tail;
  *queue->head = *queue->tail;
  SiftDown(queue, queue->head);
  ReAllocate(queue);
}

int ExcpectedCell(int x, int y, Board* board);

int Jesus(Board* board) {
  int result = 0;

  for (int x = 0; x < board->rows; ++x) {
    for (int y = 0; y < board->columns; ++y) {
      result += abs(board->data[x][y] - ExcpectedCell(x, y, board));
    }
  }

  return result;
}

int ExcpectedCell(int x, int y, Board* board) {
  return (x * board->rows + y + 1) % (board->rows * board->columns);
}

pBoard* pCopy(pBoard* in_begin, pBoard* in_end, pBoard* out_begin);

void ReAllocate(Queue* queue) {
  if (queue->size == queue->capacity || 4 * queue->size < queue->capacity) {
    int new_capacity = queue->size * 2 + 1;

    pBoard* new_head = malloc(new_capacity * sizeof(Board));
    pBoard* new_tail = pCopy(queue->head, queue->tail, new_head);

    pBoard* old_head = queue->head;

    queue->capacity = new_capacity;
    queue->head = new_head;
    queue->tail = new_tail;

    free(old_head);
  }
}

pBoard* pCopy(pBoard* in_begin, pBoard* in_end, pBoard* out_begin) {
  for (; in_begin != in_end; ++in_begin, ++out_begin) {
    *out_begin = *in_begin;
  }
  return out_begin;
}

void Print(FILE* file, const Board* board) {
  for (int x = 0; x < board->rows; ++x) {
    for (int y = 0; y < board->columns; ++y) {
      if (board->data[x][y] != EMPTY) {
        fprintf(file, "%d\t", board->data[x][y]);
      } else {
        fprintf(file, "X\t");
      }
    }
    fprintf(file, "\n");
  }
  fprintf(file, "\n");
}

void Allocate(Board* board);

Board Read(FILE* file) {
  Board board;
  fscanf(file, "%d%d", &board.rows, &board.columns);
  Allocate(&board);

  for (int x = 0; x < board.rows; ++x) {
    for (int y = 0; y < board.columns; ++y) {
      fscanf(file, "%d", &board.data[x][y]);
    }
  }

  return board;
}

void Allocate(Board* board) {
  board->data_storage = malloc(board->rows * board->columns * sizeof(Cell));
  board->data = malloc(board->rows * sizeof(Cell*));

  for (int row = 0; row < board->rows; ++row) {
    board->data[row] = board->data_storage + row * board->columns;
  }
}
