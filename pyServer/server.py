import cv2
import os
from flask import Flask, request
from gtts import gTTS
from mutagen.mp3 import MP3
from moviepy.editor import VideoFileClip, AudioFileClip, concatenate_videoclips

app = Flask(__name__)


def get_audio(verse):
    tts = gTTS(verse)
    tts.save("audio.mp3")


def create_video_from_images(images_folder, output, fps=10):
    # Get audio
    audio = MP3("audio.mp3")
    duration = audio.info.length
    # Takes image folder and sort images into array
    images = sorted(os.listdir(images_folder))
    # Create path to the input images
    img_path = os.path.join(images_folder, images[0])
    # Load image
    frame = cv2.imread(img_path)
    # Get dimensions from image
    height, width, channels = frame.shape

    # Make video
    video_writer = cv2.VideoWriter(output,
                                   cv2.VideoWriter_fourcc(*'mp4v'),
                                   fps,
                                   (width, height))

    # Get total number of frames
    frames_per_image = fps * duration

    for image in images:
        img_path = os.path.join(images_folder, image)
        frame = cv2.imread(img_path)

        for _ in range(int(frames_per_image)):
            video_writer.write(frame)

    video_writer.release()
    video_title = "finalized"
    video_clip = VideoFileClip("output.mp4")
    audio_clip = AudioFileClip("audio.mp3")
    final_clip = video_clip.set_audio(audio_clip)
    final_clip.write_videofile(video_title + '.mp4')


@app.route("/", methods=['GET'])
def get_verse():
    verse = request.args.get('verse', '')
    # Make audio file
    get_audio(verse)
    # Create video
    create_video_from_images('./images', 'output.mp4')
    # Add audio
    return f"Short Video made with the verse {verse}"
