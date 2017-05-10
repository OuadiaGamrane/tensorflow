/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.image.classifier.processor;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.tensorflow.processor.TensorflowInputConverter;
import org.springframework.cloud.stream.app.tensorflow.processor.TensorflowOutputConverter;
import org.springframework.cloud.stream.app.tensorflow.processor.TensorflowProcessorConfiguration;
import org.springframework.cloud.stream.app.tensorflow.processor.TensorflowProcessorProperties;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * A processor that evaluates a machine learning model stored in TensorFlow's ProtoBuf format.
 *
 * @author Christian Tzolov
 */
@EnableBinding(Processor.class)
@EnableConfigurationProperties({
		ImageClassifierProcessorProperties.class, TensorflowProcessorProperties.class})
@Import(TensorflowProcessorConfiguration.class)
public class ImageClassifierProcessorConfiguration {

	private static final Log logger = LogFactory.getLog(ImageClassifierProcessorConfiguration.class);

	@Autowired
	private ImageClassifierProcessorProperties properties;

	@Bean
	public TensorflowOutputConverter tensorflowOutputConverter() {
		if (logger.isInfoEnabled()) {
			logger.info("Load ImageClassifierTensorflowOutputConverter " + properties.getLabels());
		}
		return new ImageClassifierTensorflowOutputConverter(properties.getLabels(), properties.getResponseSize());
	}

	@Bean
	@RefreshScope
	public TensorflowInputConverter tensorflowInputConverter() throws MalformedURLException {
		logger.info("Load ImageClassifierTensorflowInputConverter");
		return new ImageClassifierTensorflowInputConverter();
	}
}
