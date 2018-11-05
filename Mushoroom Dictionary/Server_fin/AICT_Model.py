
from keras.applications.resnet50 import ResNet50
from keras.applications.vgg19 import VGG19
from keras.applications.xception import Xception
from keras.callbacks import ModelCheckpoint, EarlyStopping
from keras.layers import Conv2D, Dropout, Flatten, Dense, MaxPooling2D, GlobalAveragePooling2D
from keras.models import Model, Sequential
from keras.optimizers import SGD
from keras.preprocessing.image import ImageDataGenerator

model_dir = './experiment.h5'
#모델 위치

data_dir = './test'
#판단할 데이터가 있는 위치
#단 그 위치를 클릭했을때 바로 이미지가 있는 것이 아니라
#새로운 폴더안에 이미지가 들어가있어야한다.(우리가 dog, cat하듯이)
#여기서 문제 데이터를 하나씩 할지
#한꺼번에 다 받아드릴지
#사진을 찍었을 때 반응하는 건지

hyper_paras = {
    'batch_size': 16,  # 15, 45, 153
    'split_rate': 0.15,
    'img_rows': 224,
    'img_cols': 224,
    'img_channel': 3,
    'n_classes': 1,
    'epoch': 200,
}

def resnet50_model():
    #여기서 모델을 불러온다
    resnet50 = ResNet50(weights='imagenet', include_top=False, input_shape=(hyper_paras['img_rows'],
                                                                            hyper_paras['img_cols'],
                                                                            hyper_paras['img_channel']))
    model = resnet50.output
    model = Flatten(input_shape=resnet50.output_shape[1:])(model)
    model = Dense(512, activation='relu')(model)
    model = Dropout(0.5)(model)
    #6종류중에 판단하는거니깐 6
    model = Dense(6, activation='sigmoid')(model)



    model = Model(inputs=resnet50.input, outputs=model)

    #모델 변수가 저장되어있는 파일 불러오기
    model.load_weights(model_dir)

    model.compile(loss='categorical_crossentropy', optimizer=SGD(lr=1e-4, decay=1e-6, momentum=0.8, nesterov=True),
                  metrics=['accuracy'])

    #model.summary()

    return model



############################################################################################################

model_datagen = ImageDataGenerator(rescale = 1. / 255)


#여기서 데이터를 불러와야함
#근데 우리가 할 것은 데이터를 가져와서 그 데이터의 라벨은 없는 경우이다
#그리고 라벨이 없으니 input을 선택한다
#Documentation을 읽어보니 input을 선택할 때 아래와 같은 말이 있었는데
#the data still needs to reside in a subdirectory of directory for it to work correctly.
#내가 지정한 폴더안에 새로운 폴더를 만들어 그곳에다가 데이터가 저장되어있어야할 것 같다.
model_generator = model_datagen.flow_from_directory(
    data_dir,
    target_size=(hyper_paras['img_rows'], hyper_paras['img_cols']),
    batch_size = 1,
    class_mode= 'input')

model = resnet50_model()


print("-- Predict --")

output = model.predict_generator(
            model_generator,
            steps = 1)

#np.set_printoptions(formatter={'float': lambda x: "{0 : 0.3f}".format(x)})

print(output)

#여기까지만 하면 사진을 받고 사진을 모델이 판단까지한다
#그리고 나서 예측한 값을 출력해준다
#그런데 여기서 해야할 문제가 예측한 값이 아마도 (0.2, 0.0, 0.7 ...) 이렇게 나오는데-> 그냥 배열이다 output[0][0]
#이걸 어떻게 걸려낼까???
#잘만 토해내면 된다


max = 0
for num in range(0,5):
    x = output[0][num]
    if max < x:
        answer = num
        max = x

print(answer)

#형님 다 짜긴 짰습니다
#요 머냐 아직 돌아가는지는 모델이 없어서 모르겠고
#그 에러날수 있는 곳이 저 저장된 모델값 가져다가 쓰는거
#그쪽만 에러날 겁니다
#answer이 사진을 판단하고 결과값을 숫자로 내밷습니다.
#이제 안드로이드에 그 숫자를 전달해주는 방법만 생각하면 될 것 같습니다.
