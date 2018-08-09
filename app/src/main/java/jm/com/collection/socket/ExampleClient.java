package jm.com.collection.socket;
/*
 * Copyright (c) 2010-2018 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

/** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded. */
public class ExampleClient extends WebSocketClient {

	private static final String TAG="ExampleClient";
	public ExampleClient( URI serverUri , Draft draft ) {
		super( serverUri, draft );
	}

	public ExampleClient( URI serverURI ) {
		super( serverURI );
	}

	public ExampleClient( URI serverUri, Map<String, String> httpHeaders ) {
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		Log.i(TAG,"client opened connection" );
	}

	@Override
	public void onMessage( String message ) {
		Log.i(TAG, "client received: " + message );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		Log.i(TAG,"client connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
		Log.i(TAG," client error"+ex.toString());
	}

}