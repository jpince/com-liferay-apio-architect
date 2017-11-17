/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.vulcan.message.json.plain.internal;

import static com.liferay.vulcan.test.json.JsonMatchers.aJsonBoolean;
import static com.liferay.vulcan.test.json.JsonMatchers.aJsonInt;
import static com.liferay.vulcan.test.json.JsonMatchers.aJsonObjectWith;
import static com.liferay.vulcan.test.json.JsonMatchers.aJsonString;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.liferay.vulcan.test.json.Conditions;
import com.liferay.vulcan.test.resource.model.RootModel;
import com.liferay.vulcan.test.writer.MockSingleModelWriter;

import javax.ws.rs.core.HttpHeaders;

import org.hamcrest.Matcher;

import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Javier Gamarra
 * @author Alejandro Hernández
 */
public class PlainJSONSingleModelMessageMapperTest {

	@Test
	public void testMediaTypeIsCorrect() {
		String mediaType = _singleModelMessageMapper.getMediaType();

		assertThat(mediaType, is(equalTo("application/json")));
	}

	@Test
	public void testPlainJSONSingleModelMessageMapper() {
		HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);

		JsonObject jsonObject = MockSingleModelWriter.write(
			httpHeaders, _singleModelMessageMapper);

		Conditions.Builder builder = new Conditions.Builder();

		Conditions conditions = builder.where(
			"binary1", _isALinkTo("localhost:8080/b/model/first/binary1")
		).where(
			"binary2", _isALinkTo("localhost:8080/b/model/first/binary2")
		).where(
			"boolean1", is(aJsonBoolean(true))
		).where(
			"boolean2", is(aJsonBoolean(false))
		).where(
			"date1", is(aJsonString(equalTo("2016-06-15T09:00Z")))
		).where(
			"date2", is(aJsonString(equalTo("2017-04-03T18:36Z")))
		).where(
			"embedded1", _isAJsonObjectWithTheFirstEmbedded
		).where(
			"embedded2", _isALinkTo("localhost:8080/p/first-inner-model/second")
		).where(
			"link1", _isALinkTo("www.liferay.com")
		).where(
			"link2", _isALinkTo("community.liferay.com")
		).where(
			"linked1", _isALinkTo("localhost:8080/p/first-inner-model/third")
		).where(
			"linked2", _isALinkTo("localhost:8080/p/first-inner-model/fourth")
		).where(
			"localizedString1", is(aJsonString(equalTo("Translated 1")))
		).where(
			"localizedString2", is(aJsonString(equalTo("Translated 2")))
		).where(
			"number1", is(aJsonInt(equalTo(2017)))
		).where(
			"number2", is(aJsonInt(equalTo(42)))
		).where(
			"relatedCollection1",
			_isALinkTo("localhost:8080/p/model/first/models")
		).where(
			"relatedCollection2",
			_isALinkTo("localhost:8080/p/model/first/models")
		).where(
			"self", _isALinkTo("localhost:8080/p/model/first")
		).where(
			"string1", is(aJsonString(equalTo("Live long and prosper")))
		).where(
			"string2", is(aJsonString(equalTo("Hypermedia")))
		).build();

		assertThat(jsonObject, is(aJsonObjectWith(conditions)));
	}

	private static Matcher<? extends JsonElement> _isALinkTo(String url) {
		return is(aJsonString(equalTo(url)));
	}

	private static final Matcher<JsonElement>
		_isAJsonObjectWithTheFirstEmbedded;

	static {
		Conditions.Builder builder = new Conditions.Builder();

		Conditions secondEmbeddedConditions = builder.where(
			"binary",
			_isALinkTo("localhost:8080/b/second-inner-model/first/binary")
		).where(
			"boolean", is(aJsonBoolean(false))
		).where(
			"embedded", _isALinkTo("localhost:8080/p/third-inner-model/first")
		).where(
			"link", _isALinkTo("community.liferay.com")
		).where(
			"number", is(aJsonInt(equalTo(2017)))
		).where(
			"relatedCollection",
			_isALinkTo("localhost:8080/p/second-inner-model/first/models")
		).where(
			"self", _isALinkTo("localhost:8080/p/second-inner-model/first")
		).where(
			"string", is(aJsonString(equalTo("A string")))
		).where(
			"linked", _isALinkTo("localhost:8080/p/third-inner-model/second")
		).build();

		Matcher<JsonElement> isAJsonObjectWithTheSecondEmbedded = is(
			aJsonObjectWith(secondEmbeddedConditions));

		Conditions firstEmbeddedConditions = builder.where(
			"binary",
			_isALinkTo("localhost:8080/b/first-inner-model/first/binary")
		).where(
			"boolean", is(aJsonBoolean(true))
		).where(
			"link", _isALinkTo("www.liferay.com")
		).where(
			"number", is(aJsonInt(equalTo(42)))
		).where(
			"relatedCollection",
			_isALinkTo("localhost:8080/p/first-inner-model/first/models")
		).where(
			"embedded", isAJsonObjectWithTheSecondEmbedded
		).where(
			"linked", _isALinkTo("localhost:8080/p/second-inner-model/second")
		).where(
			"self", _isALinkTo("localhost:8080/p/first-inner-model/first")
		).where(
			"string", is(aJsonString(equalTo("A string")))
		).build();

		_isAJsonObjectWithTheFirstEmbedded = is(
			aJsonObjectWith(firstEmbeddedConditions));
	}

	private final PlainJSONSingleModelMessageMapper<RootModel>
		_singleModelMessageMapper = new PlainJSONSingleModelMessageMapper<>();

}