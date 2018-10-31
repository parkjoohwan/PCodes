# -*- coding: utf-8 -*-
# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

# Node 클래스 : 위치,(state) 부모노드(parent), 동작(action), 코스트의(cost) 정보를 담을 수 있음
class Node:
    def __init__(self, state, parent, action, cost):
        self.state = state
        self.parent = parent
        self.action = action
        self.cost = cost

def depthFirstSearch(problem):
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print "Start:", problem.getStartState()
    print "Is the start a goal?", problem.isGoalState(problem.getStartState())
    print "Start's successors:", problem.getSuccessors(problem.getStartState())
    """
    "*** YOUR CODE HERE ***"
    # 팩맨이 움직일 경로를 저장할 리스트
    actions = []
    # 깊이우선탐색은 stack을 사용하는 것이 편리함, LIFO
    fringe = util.Stack()
    # visited는 특정 state에 도달하기까지 방문한 state들을 전부 저장한다.
    visited = []

    start = Node(problem.getStartState(), None, None, 0)

    if problem.isGoalState(start.state):
        return actions
    # 방문 set에 현재 팩맨의 위치 추가
    visited.append(start.state)

    # 처음 위치에서 이동 가능한 위치 정보들을 getSuccessors 함수를 통해 검사
    for next in problem.getSuccessors(start.state):
        # 스택에 successor(next[0])과 start(부모노드), action(next[1]), cost를 넣는다.
        fringe.push(Node(next[0], start, next[1], 0))

    # stack이 비어있는 상태가 될 때까지 반복문
    while not fringe.isEmpty():
        # stack에서 가장 나중에 넣은 노드를 확인한다.
        lastnode = fringe.pop()
        # 나중 위치가 골이 아니라면
        if problem.isGoalState(lastnode.state):
            # 마지막 노드의 부모노드가 None이 아닐때까지(첫 노드가 아닐떄까지)
            while lastnode.parent is not None:
                # 움직임을 리스트에 넣고 마지막 노드를 마지막노드의 부모노드로 만듬(첫 노드까지)
                actions.append(lastnode.action)
                lastnode = lastnode.parent
            # 마지막 움직임부터 기록된 리스트를 역순으로 바꿔줌(마지막 움직임에서 처음 움직임으로 정렬되어있던 걸 처음부터 마지막으로)
            actions.reverse()
            # 만약 골이라면 현재까지 움직임을 return
            return actions

        # 마지막 노드의 위치가 방문 set에 없다면 set에 추가
        if lastnode.state not in visited:
            visited.append(lastnode.state)
            # 마지막 노드의 위치에서 이동 가능한 위치들을 마찬가지로 검사
            for next in problem.getSuccessors(lastnode.state):
                # 이후 스택에 넣음
                newnode = Node(next[0], lastnode, next[1], 0)
                fringe.push(newnode)
    # 최종적으로 움직임들을 반환, 만약 끝낼 수 있는 경로가 없으면 빈 리스트 반환
    return actions

def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    "*** YOUR CODE HERE ***"

    # DFS와 똑같은 알고리즘, DFS와 BFS가 차이를 보이는것은 스택과 큐의 선입선출, 후입선출의 처리 방법 때문임

    actions = []
    # BFS는 큐를 이용하면 편리하다 (FIFO)
    fringe = util.Queue()
    visited = []
    start = Node(problem.getStartState(), None, None, 0)

    if problem.isGoalState(start.state):
        return actions

    visited.append(start.state)

    for next in problem.getSuccessors(start.state):
        successornode = Node(next[0], start, next[1], 0)
        fringe.push(successornode)

    while not fringe.isEmpty():
        lastnode = fringe.pop()
        if problem.isGoalState(lastnode.state):
            while lastnode.parent is not None:
                actions.append(lastnode.action)
                lastnode = lastnode.parent
            actions.reverse()
            return actions
        if lastnode.state not in visited:
            visited.append(lastnode.state)
            for next in problem.getSuccessors(lastnode.state):
                newnode = Node(next[0], lastnode, next[1], 0)
                fringe.push(newnode)
    return actions


def uniformCostSearch(problem):
    """Search the node of least total cost first."""
    "*** YOUR CODE HERE ***"

    # 진행 알고리즘 자체는 DFS, BFS와 비슷하지만 USC는 코스트에 따른 우선순위를 부여하여 특정 노드를 먼저 빼는 차이임

    actions = []
    # UCS는 코스트에 따라 우선순위가 달라지므로 우선순위를 지정할 수 있는 우선순위 큐를 사용한다.
    fringe = util.PriorityQueue()
    visited = []

    start = Node(problem.getStartState(), None, None, 0)

    if problem.isGoalState(start.state):
        return actions

    visited.append(start.state)

    for nexts in problem.getSuccessors(start.state):
        successornode = Node(nexts[0], start, nexts[1], start.cost + nexts[2])
        fringe.push(successornode, successornode.cost)

    while not fringe.isEmpty():
        lastnode = fringe.pop()
        if problem.isGoalState(lastnode.state):
            while lastnode.parent is not None:
                actions.append(lastnode.action)
                lastnode = lastnode.parent
            actions.reverse()
            return actions

        if lastnode.state not in visited:
            visited.append(lastnode.state)
            for nexts in problem.getSuccessors(lastnode.state):
                newnode = Node(nexts[0], lastnode, nexts[1], lastnode.cost + nexts[2])
                fringe.push(newnode, newnode.cost)
    return actions


def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0


def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    "*** YOUR CODE HERE ***"
    # UCS와 매우 유사하지만, 우선순위를 부여할 때, A*는 f(n) = h(n) + g(n)의 휴리스틱과 코스트의 합을 이용함
    actions = []
    fringe = util.PriorityQueue()
    visited = []

    start = Node(problem.getStartState(), None, None, 0)

    if problem.isGoalState(start.state):
        return actions

    visited.append(start.state)

    for nexts in problem.getSuccessors(start.state):
        successornode = Node(nexts[0], start, nexts[1], start.cost + nexts[2])
        fringe.push(successornode, successornode.cost + heuristic(successornode.state, problem))

    while not fringe.isEmpty():
        lastnode = fringe.pop()
        if problem.isGoalState(lastnode.state):
            while lastnode.parent is not None:
                actions.append(lastnode.action)
                lastnode = lastnode.parent
            actions.reverse()
            return actions

        if lastnode.state not in visited:
            visited.append(lastnode.state)
            for nexts in problem.getSuccessors(lastnode.state):
                newnode = Node(nexts[0], lastnode, nexts[1], lastnode.cost + nexts[2])
                fringe.push(newnode, newnode.cost + heuristic(newnode.state, problem))
    return actions

# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch