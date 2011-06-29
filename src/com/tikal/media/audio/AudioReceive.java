package com.tikal.media.audio;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;

import com.tikal.android.media.rx.AudioPlayer;
import com.tikal.android.media.rx.MediaRx;
import com.tikal.media.AudioInfo;

public class AudioReceive implements Runnable, AudioPlayer {
	/* Implementará la interfaz definida para realizar las llamadas a FFMPEG */
	private static final String LOG_TAG = "AudioReceive";

	private int frequency =   44100;//8000;// 11025;
	private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private AudioTrack audioTrack;
	private int buffer_min;
	private int streamType;

	private String sdp;

	public AudioReceive(AudioInfo audioInfo, int streamType, String sdp) {

		Log.d(LOG_TAG,
				"AudioReceive Created: audioInfo.getOut: " + audioInfo.getOut()
						+ " ; getCodectID:" + audioInfo.getCodecID()
						+ " ; getPayLoadType:" + audioInfo.getPayloadType());
		frequency = audioInfo.getSample_rate();
		Log.d(LOG_TAG,
				"Frequency = " + frequency);
		buffer_min = AudioTrack.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);

		this.sdp = sdp;
	}

	@Override
	public void run() {
		StartReceiving();
	}

	private void StartReceiving() {
		Log.d(LOG_TAG, "Start Audio Receiving");
		audioTrack = new AudioTrack(streamType, frequency,
				channelConfiguration, audioEncoding, buffer_min,
				AudioTrack.MODE_STREAM);

		audioTrack.play();
		MediaRx.startAudioRx(sdp, this);
	}

	public void putAudio(byte[] audio, int length) {
		audioTrack.write(audio, 0, length);
	}

	public void release() {
		Log.d(LOG_TAG, "Release");
		MediaRx.stopAudioRx();
		Log.d(LOG_TAG, "ok1");
		if (audioTrack != null)
			audioTrack.release();
		Log.d(LOG_TAG, "ok2");
	}

	public void setStreamType(int streamType) {
		this.streamType = streamType;

	}

	public int getStreamType() {
		return streamType;
	}

	@Override
	public void putAudioSamplesRx(byte[] audio, int length) {
		
		if (audioTrack != null)
			audioTrack.write(audio, 0, length);

	}

}
