/*
 * Copyright 2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yang.reidis;

/**
 * Simple class keeping all the key patterns to avoid the proliferation of
 * Strings through the code.
 * 
 * @author Costin Leau
 */
abstract class KeyUtils {
	static final String USER = "user:";

	static String userIdSet() {
		return USER + "id";
	}
	
	static String userInfo(String uid) {
		return USER + "info:" + uid;
	}


}