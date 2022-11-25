# Audio emotion recognition based on Gradient Boosting classifier (4 emotions)
- The training and testing data are not included here.
- If you have any questions about the code, please email: g192e1654k@gmail.com
===========================================
# Result
- ![image](https://github.com/Evanston0624/Multimodal-Emotion-Recognition/edit/main/text/result/SER.png)

===========================================
# Introduction
# github SOURCE : https://github.com/x4nth055/emotion-recognition-using-speech

- For the speech emotion recognition model, this github uses speech emotion data in multiple languages for training.
- Training and testing datasets include: NNIME, TESS, EMODB.
- 4 emotions included : happy, angry, sad, and neutral.

- The format of each speech is monophonic, the Fs is 16000Hz.
- In this github, we have used the most used features that are available in [librosa](https://github.com/librosa/librosa) library including:
- [MFCC](https://en.wikipedia.org/wiki/Mel-frequency_cepstrum)
- MEL Spectrogram Frequency (mel).

===========================================
# Run the program

## Setting
- input = ./matched_audio/, ./matched_video/
- output = ./predict_audio_from_vid.csv, ./predict_audio.csv
- configuration = ./requirement.txt, ./conda_list.txt, ./package_version.txt

## run steps :
- step1. Delete the too small file (error) and convert the file to mono channel 16000Hz .wav.
- preprocessing_audio.py
- preprocessing_audio_from_video.py

- step2. get 4emo of audio and auidio_form_vid
- predict_audio_4emo.py
- predict_audio_from_vid_4emo.py

- step3. put ./predict_audio_from_vid.csv , ./predict_audio.csv in Proprocess_multimedia folder

===========================================