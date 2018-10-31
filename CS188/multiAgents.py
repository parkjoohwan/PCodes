#-*-coding:utf-8-*-
# multiAgents.py
# --------------
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


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):

    def getAction(self, gameState):

        # 현재 게임 상태에서 가능한 움직임들
        legalMoves = gameState.getLegalActions()

        # 최고의 움직임을 선택하기 위한 과정
        # scores 리스트에 평가 함수를 통해 얻은 score 값들을 저장함
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        # scores 리스트중에 가장 큰 점수를 찾음
        bestScore = max(scores)
        # 가장 큰 점수에 해당하는 인덱스 값을 찾아 리스트로 저장
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        # 최고 중 무작위 값을 선택함
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    # 현재 상태에서 어떠한 움직임을 취했을경우 나타나는 상태에 대한 평가 함수
    def evaluationFunction(self, currentGameState, action):
        #팩맨이 해당 action으로 움직이면 올 수 있는 다음 게임 상태에 대한 정보를 저장
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        #다음 게임 상태에서 팩맨의 위치 정보 저장
        newPos = successorGameState.getPacmanPosition()
        #다음 게임 상태에서 음식의 위치 정보 저장
        newFood = successorGameState.getFood()
        #다음 게임 상태에서 고스트의 위치 정보 저장
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        #팩맨과 음식과의 최소 거리를 구하기 위한 변수를 +무한대로 초기화
        foodDis = float("inf")

        #팩맨과 음식과의 최소 거리를 찾는 반복문
        for x in range(newFood.width):
            for y in range(newFood.height):
                if newFood[x][y] is True:
                    if(util.manhattanDistance(newPos, (x, y)) < foodDis):
                        foodDis = util.manhattanDistance(newPos, (x, y))
        #팩맨과 고스트간의 최대 거리를 구하기 위한 변수를 -무한대로 초기화
        ghostDis = float("-inf")
        # 팩맨과 고스트간의 최대 거리를 찾는 반복문
        for ghost in newGhostStates:
            if ghostDis < util.manhattanDistance(newPos, ghost.getPosition()):
                ghostDis = util.manhattanDistance(newPos, ghost.getPosition())
        #만약 ghostDis가 2보다 작으면 좋지 않은 움직임이므로 스코어 값을 -무한대 반환
        if ghostDis < 2:
            return float("-inf")
        #만약 foodDis가 1보다 작으면 좋은 움직이므로 스코어 값을 +무한대 반환
        if foodDis < 1:
            return float("inf")
        #어느 경우에도 해당되지 않으면, ghostDIs/foodDis + getScore()한 score 반환
        return ghostDis/foodDis + successorGameState.getScore()

def scoreEvaluationFunction(currentGameState):
    """
      This default evaluation function just returns the score of the state.
      The score is the same one displayed in the Pacman GUI.

      This evaluation function is meant for use with adversarial search agents
      (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
      This class provides some common elements to all of your
      multi-agent searchers.  Any methods defined here will be available
      to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

      You *do not* need to make any changes here, but you can if you want to
      add functionality to all your adversarial search agents.  Please do not
      remove anything, however.

      Note: this is an abstract class: one that should not be instantiated.  It's
      only partially specified, and designed to be extended.  Agent (game.py)
      is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
      Your minimax agent (question 2)
    """

    # 어떤 움직임을 취할지 결정하게 하는 함수
    def getAction(self, gameState):
        # 최소 값들 중에서 가장 큰 최대값을 구하는 함수로써 팩맨의 움직임을 결정하는 함수
        def maximizer(state, depth):
            # 현재의 깊이가 설정해 놓은 depth 값과 같은지, 현재 상태가 게임에서 이기거나 지는 상태인지 확인한다.
            if depth == self.depth or state.isWin() or state.isLose():
                # 위의 조건들 중 1개라도 만족한다면 평가함수에 현재의 상태를 넣고 나온 값을 반환한다.
                return self.evaluationFunction(state)
            # 최대 값을 구해야 하기 때문에 value를 -무한으로 설정해둔다.
            value = float("-inf")
            # 현재 상태에서 가능한 움직임들을 리스트에 저장한다.
            legalMoves = state.getLegalActions()
            # 현재 상태에서 가능한 모든 움직임에 반복문을 실행한다.
            for action in legalMoves:
                # 현재 상태에서 파생될수 있는 다음 상태들에 대한 minimizer 값들 중에서 최대 값을 구한다.
                value = max(value, minimizer(state.generateSuccessor(0, action), depth, 1))
            # 위에서 구한 최대 값을 반환한다.
            return value

        # 유령들의 움직임을 결정하는 함수
        def minimizer(state, depth, agentIndex):
            # 현재의 깊이가 설정해 놓은 depth 값과 같은지, 현재 상태가 게임에서 이기거나 지는 상태인지 확인한다.
            if depth == self.depth or state.isWin() or state.isLose():
                # 위의 조건들 중 1개라도 만족한다면 평가함수에 현재의 상태를 넣고 나온 값을 반환한다.
                return self.evaluationFunction(state)
            # 최소 값을 구해야 하기 때문에 value를 무한으로 설정해둔다.
            value = float("inf")
            # agentIndex를 가진 유령이 움직일수 있는 움직임들을 리스트에 저장한다.
            legalMoves = state.getLegalActions(agentIndex)
            # agentIndex가 전체 요원수 -1 인 경우 즉 마지막 유령인지를 검사한다.
            if agentIndex == state.getNumAgents() - 1:
                # 마지막 유령인 경우에는 가능한 움직임들마다 반복문을 실행한다.
                for action in legalMoves:
                    # 팩맨에게 보내기 위해서 현재 상태의 다음 state의 평가 점수들의 최댓값들 중에서의 최소값을 구한다.
                    value = min(value, maximizer(state.generateSuccessor(agentIndex, action), depth + 1))
            else:
                # 마지막 유령이 아닌 경우에도 가능한 움직임들마다 반복문을 실행하지만
                for action in legalMoves:
                    # 다음 번호의 유령에게 보내기 위해서 현재 상태의 다음 state 평가 점수들의 최소값들 중에서의 최소값을 구한다.
                    value = min(value,
                                minimizer(state.generateSuccessor(agentIndex, action), depth, agentIndex + 1))
            # 위에서 구한 최소값을 반환한다.
            return value

        # 현재 게임 상태에서 가능한 움직임들을 리스트로 저장한다.
        legalMoves = gameState.getLegalActions()
        # 현재의 움직임은 정지 상태로 선언한다.
        move = Directions.STOP
        # 최대 값을 구해야 하기 때문에 value를 -무한으로 설정해둔다.
        value = float("-inf")
        # 가능한 움직임마다 반복문을 실행한다.
        for action in legalMoves:
            # 다음 상태의 평가 값들중에서 최소값을 구하여 저장한다.
            temp = minimizer(gameState.generateSuccessor(0, action), 0, 1)
            # 최소값들중에서 최대 값을 구하기 위해서 temp와 value를 비교한다.
            if temp > value:
                # temp가 크다면 value 값을 temp 값으로 교환한다.
                value = temp
                # temp값을 얻기위해서 해야할 움직임을 move에 저장한다.
                move = action
        # 팩맨의 움직임을 반환한다.
        return move

class AlphaBetaAgent(MultiAgentSearchAgent):
    # 어떻게 움직이는지 결정하는 함수
    def getAction(self, gameState):
        # 현재 게임 상태와 알파 값은 -무한대, 베타 값은 +무한대로, agentIndex와, depth값을 0으로하여 실행된 값의 2번째 요소(action) 반환
        return self.val(gameState, -float("inf"), float("inf"), self.index, 0)[1]
    # Alpha-Beta Implementation 수도 코드대로
    # value 함수에서는 terminal state인지 확인하고, 그것의 평가값을 반환하거나
    # 다음 agent가 MAX라면 maxvalue 함수를 실행하여 반환하고
    # 다음 agent가 MIN이라면 minvalue 함수를 실행하여 반환한다.
    def val(self, gameState, a, b, agentIndex, depth):
        # 현재 게임 상태가 이기거나 졌는지 판단 해당되면 반환
        if gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState), None
        # 마지막 agent까지 진행되었는지 검사, 진행됬다면 depth+1
        if agentIndex % gameState.getNumAgents() == 0 and agentIndex != 0:
            agentIndex = 0
            depth += 1
        # 진행된 깊이가 설정된 깊이와 같다면 반환
        if depth == self.depth:
            return self.evaluationFunction(gameState), None
        #다음 agent가 max이면
        if agentIndex % gameState.getNumAgents() == 0:
            return self.maxValue(gameState, a, b, agentIndex, depth)
        # 다음 agent가 max가 아니면 (min 이면)
        else:
            return self.minValue(gameState, a, b, agentIndex, depth)

    def maxValue(self, gameState, a, b, agentIndex, depth):
        # v 값을 -무한대로 초기화 action은 아무것도
        v = (-float("inf"), None)
        # 가능한 움직임에 대한 반복문
        for action in gameState.getLegalActions(agentIndex):
            # 다음 상태들에 대한 val 함수 실행
            successor = gameState.generateSuccessor(agentIndex, action)
            newV = self.val(successor, a, b, agentIndex+1, depth)
            # 이후 max 값을 구함
            vIns = max(newV[0], v[0])
            # vlns가 newV[0]값과 같다면 v = (newV[0], action)을 넣고
            if vIns == newV[0]:
                v = (newV[0], action)
            # v[0]이 베타 값보다 크다면 v를 반환함
            if v[0] > b:
                return v
            # 아니라면 알파값과 v[0]값중 큰 값을 알파값으로 지정
            a = max(a, v[0])
        return v

    def minValue(self, gameState, a, b, agentIndex, depth):
        # v 값을 +무한대로 초기화 action은 아무것도
        v = (float("inf"), None)
        # 가능한 움직임에 대한 반복문
        for action in gameState.getLegalActions(agentIndex):
            # 다음 상태들에 대한 val 함수 실행
            successor = gameState.generateSuccessor(agentIndex, action)
            newV = self.val(successor, a, b, agentIndex+1, depth)
            # 이후 min 값을 구함
            vIns = min(newV[0], v[0])
            #vlns가 newV[0]값과 같다면 v = (newV[0], action)으로 지정
            if vIns == newV[0]:
                v = (newV[0], action)
            #v[0]가 알파 값보다 작다면 v를 반환
            if v[0] < a:
                return v
            # 아니라면 베타값과 v[0]중 작은값을 베타값으로 지정
            b = min(b, v[0])
        return v




class ExpectimaxAgent(MultiAgentSearchAgent):
    # 어떤 움직임을 취할지 결정하게 하는 함수
    def getAction(self, gameState):
        # 추정값들 중에서 가장 큰 최대값을 구하는 함수로써 팩맨의 움직임을 결정하는 함수
        def maximizer(state, depth):
            # 현재의 깊이가 설정해 놓은 depth 값과 같은지, 현재 상태가 게임에서 이기거나 지는 상태인지 확인한다.
            if depth==self.depth or state.isWin() or state.isLose():
                # 위의 조건들 중 1개라도 만족한다면 평가함수에 현재의 상태를 넣고 나온 값을 반환한다.
                return self.evaluationFunction(state)
            # 최대 값을 구해야 하기 때문에 value를 -무한으로 설정해둔다.
            value = float("-inf")
            # 현재 상태에서 가능한 움직임들을 리스트에 저장한다.
            legalMoves = state.getLegalActions()
            # 현재 상태에서 가능한 모든 움직임에 반복문을 실행한다.
            for action in legalMoves:
                # 현재 상태에서 파생될수 있는 다음 상태들에 대한 minimizer 값들 중에서 최대 값을 구한다.
                value = max(value, expecter(state.generateSuccessor(0, action), depth, 1))
            # 위에서 구한 최대 값을 반환한다.
            return value
        # 유령들의 움직임을 결정하는 함수
        def expecter(state, depth, agentIndex):
            # 현재의 깊이가 설정해 놓은 depth 값과 같은지, 현재 상태가 게임에서 이기거나 지는 상태인지 확인한다.
            if depth==self.depth or state.isWin() or state.isLose():
                # 위의 조건들 중 1개라도 만족한다면 평가함수에 현재의 상태를 넣고 나온 값을 반환한다.
                return self.evaluationFunction(state)
            # 추정 값은 전체 평가값들을 더한 뒤 개수로 나누어 평균값을 구한다. 추정 값을 구해야 하기 때문에 value를 0으로 설정해두고 이 값에 평가값들을 더해 나간다.
            value = 0
            # agentIndex를 가진 유령이 움직일수 있는 움직임들을 리스트에 저장한다.
            legalMoves = state.getLegalActions(agentIndex)
            # agentIndex가 전체 요원수 -1 인 경우 즉 마지막 유령인지를 검사한다.
            if agentIndex==state.getNumAgents()-1:
                # 마지막 유령인 경우에는 가능한 움직임들마다 반복문을 실행한다.
                for action in legalMoves:
                    # 팩맨에게 보내기 위해서 현재 상태의 다음 state의 평가 점수들의 최댓값들 중에서의 최소값을 구한다.
                    value +=  maximizer(state.generateSuccessor(agentIndex, action), depth+1)
            else:
                # 마지막 유령이 아닌 경우에도 가능한 움직임들마다 반복문을 실행하지만
                for action in legalMoves:
                    # 다음 번호의 유령에게 보내기 위해서 현재 상태의 다음 state 평가 점수들의 최소값들 중에서의 최소값을 구한다.
                    value += expecter(state.generateSuccessor(agentIndex, action), depth, agentIndex+1)
            # 위에서 구한 최소값을 반환한다.
            return value/len(legalMoves)
        # 현재 게임 상태에서 가능한 움직임들을 리스트로 저장한다.
        legalMoves = gameState.getLegalActions()
        # 현재의 움직임은 정지 상태로 선언한다.
        move = Directions.STOP
        # 최대 값을 구해야 하기 때문에 value를 -무한으로 설정해둔다.
        value = float("-inf")
        # 가능한 움직임마다 반복문을 실행한다.
        for action in legalMoves:
            # 다음 상태의 평가 값들중에서 추정값을 구하여 저장한다.
            temp = expecter(gameState.generateSuccessor(0, action), 0, 1)
            # 추정 값들중에서 최대 값을 구하기 위해서 temp와 value를 비교한다.
            if temp > value:
                # temp가 크다면 value 값을 temp 값으로 교환한다.
                value = temp
                # temp값을 얻기위해서 해야할 움직임을 move에 저장한다.
                move = action
        # 팩맨의 움직임을 반환한다.
        return move

def betterEvaluationFunction(currentGameState):
    # 현재 게임 상태에서 팩맨의 위치 정보를 받아서 저장
    currentPos = currentGameState.getPacmanPosition()
    # 현재 게임 상태에서 음식의 위치 정보를 받아서 저장
    currentFood = currentGameState.getFood()
    # 현재 게임 상태에서 캡슐 아이템의 위치 정보를 받아서 저장
    capsulePos = currentGameState.getCapsules()
    # 현재 게임 상태에서 맵의 높이와 너비를 구하기 위해서 벽의 정보를 받아와서 저장
    layout = currentGameState.getWalls()
    # 게임 상에서 팩맨과 유령 혹은 음식이 가장 멀리 있는 경우는 맵의 양 대각선 끝쪽이기 때문에 맵의 높이와 너비를 더한다.
    maxlength = layout.height - 2 + layout.width - 2
    # 음식들과 팩맨사이의 거리들을 저장하기 위한 리스트 선언
    fooddistance = []
    # 캡슐 아이템과 팩맨사이의 거리들을 저장하기 위한 리스트 선언
    capsuledistance = []
    # 음식마다 반복문을 실행
    for food in currentFood.asList():
        # 팩맨과 음식의 위치간의 거리를 맨하탄 Distance 함수를 이용해서 구한후 리스트에 넣는다.
        fooddistance.append(manhattanDistance(currentPos, food))
    # 캡슐 아이템마다 반복문을 실행
    for capsule in capsulePos :
        # 팩맨과 캡슐 아이템의 위치간의 거리를 맨하탄 Distance 함수를 이용해서 구한후 리스트에 넣는다.
        capsuledistance.append(manhattanDistance(currentPos, capsule))
    # 상태 평가를 위한 점수를 0으로 초기화한 후 선언
    score = 0
    # 현재 팩맨의 위치 정보중 x좌표를 저장
    x = currentPos[0]
    # 현재 팩맨의 위치 정보중 y좌표를 저장
    y = currentPos[1]
    # 모든 유령에 대하여 반복문 실행
    for ghostState in currentGameState.getGhostStates():
        # 유령과 팩맨의 위치간의 거리를 맨하탄 Distance 함수를 이용해서 구한다.
        gd = manhattanDistance(currentPos, ghostState.configuration.getPosition())
        # 유령과의 거리가 2보다 작은지 확인
        if gd < 2:
            # 캡슐아이템을 먹어서 유령이 현재 먹을수 있는 상태인지 확인
            if ghostState.scaredTimer != 0:
                # 먹을 수 있는 상태의 유령이라면 가까이 갈 수록 상태점수가 높아지게 설정하여 팩맨이 유령을 먹으러 가게끔 만든다.
                score += 1000.0/(gd+1)
            else :
                # 먹을 수 없는 상태의 유령이라면 가까이 갈 수록 상태점수가 낮아지게 설정하여 팩맨이 도망치게 만든다.
                score -= 1000.0/(gd+1)
    # 캡슐과의 최소 거리가 5보다 작은지 확인한다. 이때 캡슐이 하나도 존재하지 않을 수 있으니 리스트에 추가적으로 float(100)을 추가한다.
    if min(capsuledistance+[float(100)])<5:
        # 캡슐과의 거리가 가까워질 수록 상태점수가 높아지게 설정
        score += 500.0/(min(capsuledistance))
    # 위의 조건문에서 캡슐과의 거리가 작아질 수록 상태점수가 높아지지만 팩맨이 캡슐을 먹게되면 상태점수가 높아지지 않아서 팩맨이 캡슐을
    # 먹지 않는 경우가 발생하므로 캡슐의 위치와 팩맨의 위치가 같은지 검사하여 만약 팩맨이 캡슐을 먹었다면 더 높은 상태점수를 갖게 설정
    for capsule in capsulePos:
        # 팩맨의 위치와 캡슐의 위치가 같은지 확인
        if (capsule[0]==x)&(capsule[1]==y):
            # 팩맨이 캡슐을 먹었다면 상태점수가 높아지게 설정하여 팩맨이 캡슐 아이템을 먹게끔 만든다.
            score += 600.0
    # 음식과 팩맨간의 최소거리를 구한다. 이때 음식이 하나도 없는 경우를 측정하기 위해서 float(100)을 추가한다.
    minfooddistance = min(fooddistance+[float(100)])
    # 위에서 설정한 상태점수에 음식과 팩맨간의 최소거리의 역수를 더한 후 남아있는 음식의 개수에 10을 곱한 값을 뺀 후 상태점수를 반환한다.
    return score + 1.0/minfooddistance - len(fooddistance)*10.0

# Abbreviation
better = betterEvaluationFunction

