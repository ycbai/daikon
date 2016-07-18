package fr.talend.logs.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class ResettableServletInputStream extends ServletInputStream {

	private InputStream stream;

	@Override
	public int read() throws IOException {
		return stream.read();
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		//nothing to do
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	
}
