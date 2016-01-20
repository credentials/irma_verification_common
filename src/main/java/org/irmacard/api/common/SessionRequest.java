package org.irmacard.api.common;

import org.irmacard.api.common.util.GsonUtil;
import org.irmacard.credentials.idemix.IdemixSystemParameters;
import org.irmacard.credentials.idemix.util.Crypto;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.SecureRandom;

public abstract class SessionRequest implements Serializable {
	private static final long serialVersionUID = -1765381227944439300L;

	private BigInteger nonce;
	private BigInteger context;

	public SessionRequest(BigInteger nonce, BigInteger context) {
		this.nonce = nonce;
		this.context = context;
	}

	public BigInteger getNonce() {
		return nonce;
	}

	public void setNonce(BigInteger nonce) {
		this.nonce = nonce;
	}

	public BigInteger getContext() {
		return context;
	}

	public void setContext(BigInteger context) {
		this.context = context;
	}

	public static BigInteger generateNonce() {
		return new BigInteger(new IdemixSystemParameters().l_statzk, new SecureRandom());
	}

	public BigInteger generateContext() {
		return Crypto.sha256Hash(toString(false).getBytes());
	}

	public void setNonceAndContext() {
		this.nonce = generateNonce();
		this.context = generateContext();
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean includeContext) {
		BigInteger context = this.context;
		BigInteger nonce = this.nonce;
		if (!includeContext) {
			this.context = null;
			this.nonce = null;
		}

		String val = GsonUtil.getGson().toJson(this);

		this.context = context;
		this.nonce = nonce;

		return val;
	}
}