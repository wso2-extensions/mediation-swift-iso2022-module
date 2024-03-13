/**
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 * <p>
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.module.swiftiso20022;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.module.swiftiso20022.constants.ConnectorConstants;
import org.wso2.carbon.module.swiftiso20022.constants.MT103Constants;
import org.wso2.carbon.module.swiftiso20022.models.mt103models.MT103JsonRequestPayload;
import org.wso2.carbon.module.swiftiso20022.utils.ConnectorUtils;
import org.wso2.carbon.module.swiftiso20022.utils.JsonToMt103Utils;
import org.wso2.carbon.module.swiftiso20022.validation.common.ValidationResult;

import java.util.Optional;


/**
 * Class to convert JSON Object to MT103 format.
 */
public class JsonToMT103Validator extends AbstractConnector {

    private static final Gson gson = new Gson();

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        this.log.debug("Executing JsonToMT103Transformer to convert the JSON payload to MT103 format");
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();

        // get JSON payload as a string from the message context
        Optional<String> payload = ConnectorUtils.buildMessagePayloadFromMessageContext(axis2MessageContext);

        // if JSON payload is available
        if (payload.isPresent()) {

            // check JSON object with request model (data types of fields)
            try {
                validatePayloadWithMT103JsonModel(payload.get());
            } catch (JsonSyntaxException e) {
                this.log.error(ConnectorConstants.ERROR_INVALID_PAYLOAD, e);
                ConnectorUtils.appendErrorToMessageContext(
                        messageContext, ConnectorConstants.INVALID_REQUEST_PAYLOAD, e.getMessage());
                super.handleException(ConnectorConstants.ERROR_INVALID_PAYLOAD, messageContext);
            }

            // validate values of the JSON object
            this.log.debug("Validating payload parameters");
            ValidationResult validationResponse = validateRequestPayload(payload.get());
            if (validationResponse.isInvalid()) {
                this.log.error(validationResponse.getErrorMessage());
                ConnectorUtils.appendErrorToMessageContext(messageContext, validationResponse.getErrorCode(),
                        validationResponse.getErrorMessage());
                super.handleException(ConnectorConstants.ERROR_VALIDATION_FAILED, messageContext);
            }
        } else {
            this.log.error(ConnectorConstants.ERROR_MISSING_PAYLOAD);
            ConnectorUtils.appendErrorToMessageContext(messageContext, ConnectorConstants.MISSING_REQUEST_PAYLOAD,
                    ConnectorConstants.ERROR_MISSING_PAYLOAD);
            super.handleException(ConnectorConstants.ERROR_MISSING_PAYLOAD, messageContext);
        }
    }

    private ValidationResult validateRequestPayload(String payloadString) {

        // build JSONObject from the payload string
        JSONObject requestPayload = new JSONObject(payloadString);

        // validate block 01
        ValidationResult blockValidationResult =
                JsonToMt103Utils.validateBlock01(requestPayload.optJSONObject(MT103Constants.BLOCK01));
        if (blockValidationResult.isInvalid()) {
            return blockValidationResult;
        }

        // validate block 02
        blockValidationResult = JsonToMt103Utils.validateBlock02(requestPayload.optJSONObject(MT103Constants.BLOCK02));
        if (blockValidationResult.isInvalid()) {
            return blockValidationResult;
        }

        // validate block 03
        blockValidationResult = JsonToMt103Utils.validateBlock03(requestPayload.optJSONObject(MT103Constants.BLOCK03));
        if (blockValidationResult.isInvalid()) {
            return blockValidationResult;
        }

        // validate block 04
        blockValidationResult = JsonToMt103Utils.validateBlock04(requestPayload.optJSONObject(MT103Constants.BLOCK04));
        if (blockValidationResult.isInvalid()) {
            return blockValidationResult;
        }

        // validate block 05
        // validation result is returned since block 05 is the last block
        return JsonToMt103Utils.validateBlock05(requestPayload.optJSONObject(MT103Constants.BLOCK05));
    }

    // validate JSON object with the defined request model
    private void validatePayloadWithMT103JsonModel(String payload) throws JsonSyntaxException {
        this.log.debug("Validating JSON payload with the request model");
        gson.fromJson(payload, MT103JsonRequestPayload.class);
    }
}
