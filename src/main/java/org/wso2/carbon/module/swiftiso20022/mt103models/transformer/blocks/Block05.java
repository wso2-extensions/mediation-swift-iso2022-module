/**
 * Copyright (c) 2024, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.module.swiftiso20022.mt103models.transformer.blocks;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.module.swiftiso20022.model.ErrorModel;

/**
 * Class that models request payload block05.
 */
public class Block05 implements RequestPayloadBlock {
    static final String BLOCK_NAME = "block05";
    String checksum;
    String testNTrainingMessage;
    String possibleDuplicateEmission;
    String delayedMessage;
    String messageReference;
    String possibleDuplicateMessage;
    String systemOriginatedMessage;

    @Override
    public ErrorModel validate() {
        // TODO: replace with constants
        if (StringUtils.isBlank(checksum)) {
            return new ErrorModel("Z04", "Checksum is required");
        }
        if (checksum.length() != 12) {
            return new ErrorModel("Z04", "Checksum length is invalid");
        }
        if (!StringUtils.isBlank(possibleDuplicateEmission) && possibleDuplicateEmission.length() != 32) {
            return new ErrorModel("Z05", "Possible Duplicate Emission length is invalid");
        }
        if (!StringUtils.isBlank(messageReference) && messageReference.length() != 38) {
            return new ErrorModel("Z00", "Message Reference length is invalid");
        }
        if (!StringUtils.isBlank(possibleDuplicateMessage) && possibleDuplicateMessage.length() != 32) {
            return new ErrorModel("Z00", "Possible Duplicate Message length is invalid");
        }
        if (!StringUtils.isBlank(systemOriginatedMessage) && systemOriginatedMessage.length() != 32) {
            return new ErrorModel("Z00", "System Originated Message length is invalid");
        }
        return new ErrorModel();
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setTestNTrainingMessage(String testNTrainingMessage) {
        this.testNTrainingMessage = testNTrainingMessage;
    }

    public String getTestNTrainingMessage() {
        return testNTrainingMessage;
    }

    public void setPossibleDuplicateEmission(String possibleDuplicateEmission) {
        this.possibleDuplicateEmission = possibleDuplicateEmission;
    }

    public String getPossibleDuplicateEmission() {
        return possibleDuplicateEmission;
    }

    public void setDelayedMessage(String delayedMessage) {
        this.delayedMessage = delayedMessage;
    }

    public String getDelayedMessage() {
        return delayedMessage;
    }

    public void setMessageReference(String messageReference) {
        this.messageReference = messageReference;
    }

    public String getMessageReference() {
        return messageReference;
    }

    public void setPossibleDuplicateMessage(String possibleDuplicateMessage) {
        this.possibleDuplicateMessage = possibleDuplicateMessage;
    }

    public String getPossibleDuplicateMessage() {
        return possibleDuplicateMessage;
    }

    public void setSystemOriginatedMessage(String systemOriginatedMessage) {
        this.systemOriginatedMessage = systemOriginatedMessage;
    }

    public String getSystemOriginatedMessage() {
        return systemOriginatedMessage;
    }
}
