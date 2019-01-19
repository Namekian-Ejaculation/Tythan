package co.lotc.core;

import java.io.File;

import lombok.RequiredArgsConstructor;

/**
 * Serves as the object performing common actions for both Tythan implementations
 */
@RequiredArgsConstructor
public class TythanCommon {
	private final Tythan tythan;
	
	public void onLoad() {
		TythanProvider.init(tythan);
	}
	
}
