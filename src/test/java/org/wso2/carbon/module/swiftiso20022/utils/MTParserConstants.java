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

package org.wso2.carbon.module.swiftiso20022.utils;

import org.testng.annotations.DataProvider;
import org.wso2.carbon.module.swiftiso20022.constants.ConnectorConstants;
import org.wso2.carbon.module.swiftiso20022.mt.models.blocks.ApplicationHeaderBlock;
import org.wso2.carbon.module.swiftiso20022.mt.models.blocks.BasicHeaderBlock;
import org.wso2.carbon.module.swiftiso20022.mt.models.mtmessages.MT940Message;
import org.wso2.carbon.module.swiftiso20022.mt.models.mtmessages.MTMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Test constants for MTParser.
 */
public class MTParserConstants {
    public static String getBasicHeaderBlockText(Map<String, String> params) {
        return  (params.getOrDefault("AppID", "F")) +
                (params.getOrDefault("ServiceID", "01")) +
                (params.getOrDefault("LTAddress", "GSCRUS30XXXX")) +
                (params.getOrDefault("SessionNumber", "0000")) +
                (params.getOrDefault("SequenceNumber", "000000"));
    }

    public static String getApplicationHeaderBlockText(Map<String, String> params) {
        if (params.containsKey("IOID") && "I".equals(params.get("IOID"))) {
            // Given message is input message
            return  (params.getOrDefault("IOID", "I")) +
                    (params.getOrDefault("MessageType", "103")) +
                    (params.getOrDefault("DestinationAddress", "GSCRUS30XXXX")) +
                    (params.getOrDefault("Priority", "U")) +
                    (params.getOrDefault("DeliveryMonitor", "3")) +
                    (params.getOrDefault("ObsolescencePeriod", "003"));
        } else {
            // Given message is output message(default)
            return  (params.getOrDefault("IOID", "O")) +
                    (params.getOrDefault("MessageType", "940")) +
                    (params.getOrDefault("InputTime", "0400")) +
                    (params.getOrDefault("MessageInputReference", "190425GSCRUS30XXXX0000000000")) +
                    (params.getOrDefault("OutputDate", "240327")) +
                    (params.getOrDefault("OutputTime", "1128")) +
                    (params.getOrDefault("Priority", "N"));
        }
    }

    public static Map<String, String> getMTMessageMap(Map<String, String> params) {
        Map<String, String> blocks = new HashMap<>();

        if (params.containsKey(ConnectorConstants.BASIC_HEADER_BLOCK_KEY)) {
            blocks.put(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                    params.get(ConnectorConstants.BASIC_HEADER_BLOCK_KEY));
        }

        if (params.containsKey(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY)) {
            blocks.put(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                    params.get(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY));
        }

        if (params.containsKey(ConnectorConstants.USER_HEADER_BLOCK_KEY)) {
            blocks.put(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                    params.get(ConnectorConstants.USER_HEADER_BLOCK_KEY));
        }

        if (params.containsKey(ConnectorConstants.TEXT_BLOCK_KEY)) {
            blocks.put(ConnectorConstants.TEXT_BLOCK_KEY,
                    params.get(ConnectorConstants.TEXT_BLOCK_KEY));
        }

        if (params.containsKey(ConnectorConstants.TRAILER_BLOCK_KEY)) {
            blocks.put(ConnectorConstants.TRAILER_BLOCK_KEY,
                    params.get(ConnectorConstants.TRAILER_BLOCK_KEY));
        }

        return blocks;
    }

    public static BasicHeaderBlock getBasicHeaderBlock(Map<String, String> params) {
        BasicHeaderBlock basicHeaderBlock = new BasicHeaderBlock();

        basicHeaderBlock.setApplicationIdentifier(params.getOrDefault("AppID", null));
        basicHeaderBlock.setServiceIdentifier(params.getOrDefault("ServiceID", null));
        basicHeaderBlock.setLogicalTerminalAddress(params.getOrDefault("LTAddress", null));
        basicHeaderBlock.setSessionNumber(params.getOrDefault("SessionNumber", null));
        basicHeaderBlock.setSequenceNumber(params.getOrDefault("SequenceNumber", null));

        return basicHeaderBlock;
    }

    public static ApplicationHeaderBlock getApplicationHeaderBlock(Map<String, String> params) {
        ApplicationHeaderBlock applicationHeaderBlock = new ApplicationHeaderBlock();

        applicationHeaderBlock.setInputOutputIdentifier(params.getOrDefault("IOID", null));
        applicationHeaderBlock.setMessageType(params.getOrDefault("MessageType", null));
        // Input message related blocks
        applicationHeaderBlock.setDestinationAddress(params.getOrDefault("DestinationAddress", null));
        applicationHeaderBlock.setPriority(params.getOrDefault("Priority", null));
        applicationHeaderBlock.setDeliveryMonitor(params.getOrDefault("DeliveryMonitor", null));
        applicationHeaderBlock.setObsolescencePeriod(params.getOrDefault("ObsolescencePeriod", null));
        // Output message related blocks
        applicationHeaderBlock.setInputTime(params.getOrDefault("InputTime", null));
        applicationHeaderBlock.setMessageInputReference(params.getOrDefault("MessageInputReference",
                null));
        applicationHeaderBlock.setOutputDate(params.getOrDefault("OutputDate", null));
        applicationHeaderBlock.setOutputTime(params.getOrDefault("OutputTime", null));
        // Setting the priority
        applicationHeaderBlock.setPriority(params.getOrDefault("Priority", null));

        return applicationHeaderBlock;
    }

    public static <T extends MTMessage> T getMTMessage(Map<String, Object> params, Class<T> messageType)
            throws Exception {
        T message = messageType.getConstructor().newInstance();

        message.setBasicHeaderBlock((BasicHeaderBlock) params.getOrDefault(
                ConnectorConstants.BASIC_HEADER_BLOCK_KEY, null));
        message.setApplicationHeaderBlock((ApplicationHeaderBlock) params.getOrDefault(
                ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY, null));

        return message;
    }

    public static String getMTMessageText(Map<String, String> params) {
        return  (params.getOrDefault(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                "{1:F01GSCRUS30XXXX0000000000}")) +
                (params.getOrDefault(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                "{2:O9400400190425GSCRUS30XXXX00000000002403290912N}")) +
                (params.getOrDefault(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                "{3:{113:URGT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}")) +
                (params.getOrDefault(ConnectorConstants.TEXT_BLOCK_KEY, "{4:\n:20:258158850\n" +
                ":21:258158850\n" +
                ":25:DD01100056869\n" +
                ":28C:1/1\n" +
                ":60F:D230930USD843686,20\n" +
                ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001DD10,00ACHPNONREF\n" +
                ":86:PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001CD10,00ASHP20230928LTERMID2000003\n" +
                ":86:EREF/20230928LTERMID2000003/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":62F:D230930USD846665,15\n" +
                ":64:C231002USD334432401,27\n-}")) +
                (params.getOrDefault(ConnectorConstants.TRAILER_BLOCK_KEY, "{5:{CHK:123456789ABC}}"));
    }

    @DataProvider(name = "parseMTMessage")
    Object[][] parseMTMessage() throws Exception {
        return new Object[][] {
                {getMTMessageText(Map.of()),
                 getMTMessage(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                         getBasicHeaderBlock(Map.of(
                         "AppID", "F", "ServiceID", "01", "LTAddress", "GSCRUS30XXXX",
                         "SessionNumber", "0000", "SequenceNumber", "000000")),
                         ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                         getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                 "InputTime", "0400", "MessageInputReference",
                                 "190425GSCRUS30XXXX0000000000", "OutputDate", "240329"
                                 , "OutputTime", "0912", "Priority", "N"))), MT940Message.class)
                },
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY, "")),
                        getMTMessage(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                                getBasicHeaderBlock(Map.of("AppID", "F", "ServiceID", "01",
                                        "LTAddress", "GSCRUS30XXXX", "SessionNumber", "0000",
                                        "SequenceNumber", "000000"))), MT940Message.class)
                }
        };
    }

    @DataProvider(name = "parseInvalidMTMessage")
    Object[][] parseInvalidMTMessage() throws Exception {
        return new Object[][] {
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, "")),
                        getMTMessage(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                                getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                        "InputTime", "0400", "MessageInputReference",
                                        "190425GSCRUS30XXXX0000000000", "OutputDate", "240329"
                                        , "OutputTime", "0912", "Priority", "N"))), MT940Message.class)
                },
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, "",
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY, "")),
                        getMTMessage(Map.of(), MT940Message.class)
                },
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, "{1:}",
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY, "{2:}")),
                        getMTMessage(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                                getBasicHeaderBlock(Map.of()),
                                ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                                getApplicationHeaderBlock(Map.of())), MT940Message.class)
                },
        };
    }

    @DataProvider(name = "parseMTMessageBlocks")
    Object[][] parseMTMessageBlocks() {
        return new Object[][] {
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                "{1:F01GSCRUS30XXXX0000000000}",
                ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                "{2:O9400400190425GSCRUS30XXX00000000002403290912N}")),
                getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                "F01GSCRUS30XXXX0000000000",
                ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                "O9400400190425GSCRUS30XXX00000000002403290912N",
                ConnectorConstants.USER_HEADER_BLOCK_KEY,
                "{113:URGT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}",
                ConnectorConstants.TEXT_BLOCK_KEY,
                "\n:20:258158850\n" +
                ":21:258158850\n" +
                ":25:DD01100056869\n" +
                ":28C:1/1\n" +
                ":60F:D230930USD843686,20\n" +
                ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001DD10,00ACHPNONREF\n" +
                ":86:PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001CD10,00ASHP20230928LTERMID2000003\n" +
                ":86:EREF/20230928LTERMID2000003/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":62F:D230930USD846665,15\n" +
                ":64:C231002USD334432401,27\n",
                ConnectorConstants.TRAILER_BLOCK_KEY, "{CHK:123456789ABC}"))},

                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                "{1:F01GSCRUS30XXXX0000000000}",
                ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY, "")),
                getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                "F01GSCRUS30XXXX0000000000",
                ConnectorConstants.USER_HEADER_BLOCK_KEY,
                "{113:URGT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}",
                ConnectorConstants.TEXT_BLOCK_KEY,
                "\n:20:258158850\n" +
                ":21:258158850\n" +
                ":25:DD01100056869\n" +
                ":28C:1/1\n" +
                ":60F:D230930USD843686,20\n" +
                ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001DD10,00ACHPNONREF\n" +
                ":86:PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001CD10,00ASHP20230928LTERMID2000003\n" +
                ":86:EREF/20230928LTERMID2000003/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":62F:D230930USD846665,15\n" +
                ":64:C231002USD334432401,27\n",
                ConnectorConstants.TRAILER_BLOCK_KEY, "{CHK:123456789ABC}"))},

                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY, "")),
                getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                "F01GSCRUS30XXXX0000000000",
                ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                "O9400400190425GSCRUS30XXX00000000002403290912N",
                ConnectorConstants.TEXT_BLOCK_KEY,
                "\n:20:258158850\n" +
                ":21:258158850\n" +
                ":25:DD01100056869\n" +
                ":28C:1/1\n" +
                ":60F:D230930USD843686,20\n" +
                ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001DD10,00ACHPNONREF\n" +
                ":86:PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001CD10,00ASHP20230928LTERMID2000003\n" +
                ":86:EREF/20230928LTERMID2000003/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":62F:D230930USD846665,15\n" +
                ":64:C231002USD334432401,27\n",
                ConnectorConstants.TRAILER_BLOCK_KEY, "{CHK:123456789ABC}"))},

                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY, "")),
                getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                "F01GSCRUS30XXXX0000000000",
                ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                "O9400400190425GSCRUS30XXX00000000002403290912N",
                ConnectorConstants.USER_HEADER_BLOCK_KEY,
                "{113:URGT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}",
                ConnectorConstants.TEXT_BLOCK_KEY,
            "\n:20:258158850\n" +
                ":21:258158850\n" +
                ":25:DD01100056869\n" +
                ":28C:1/1\n" +
                ":60F:D230930USD843686,20\n" +
                ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001DD10,00ACHPNONREF\n" +
                ":86:PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":61:2310011001CD10,00ASHP20230928LTERMID2000003\n" +
                ":86:EREF/20230928LTERMID2000003/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                ":62F:D230930USD846665,15\n" +
                ":64:C231002USD334432401,27\n"))}
        };
    }

    @DataProvider(name = "parseInvalidMTMessageBlocks")
    Object[][] parseInvalidMTMessageBlocks() {
        return new Object[][] {
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, ""))},
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        "{1:F01GSCRUS30XXXX0000000000"))},
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        "{1:"))},
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        "{1:}"))},
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        "{1:F01GSCRUS30XXXX0000000000}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        "{1:{{F01GSCRUS30XXXX0000000000}"))},
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        "{1:F01GSCRUS30XXXX{}0000000000}"))},
                {getMTMessageText(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        "{1:F01GSCRUS30XXXX}{0000000000}"))},
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        "{2:O9400400190425GSCRUS30XXX0000000002403141137N"))},
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        "{2:"))},
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        "{2:}"))},
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        "{2:O9400400190425GSCRUS30XXX0000000002403141137N}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        "{2:{{O9400400190425GSCRUS30XXX0000000002403141137N}"))},
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        "{2:O9400400190425GSCRUS30XXX{}0000000002403141137N}"))},
                {getMTMessageText(Map.of(ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        "{2:O9400400190425GSCRUS30XXX}{0000000002403141137N}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:URGT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:URGT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:URGT}}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:URGT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:URGT}{108:INTLPMTS}{}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:URGT}{108:INTLPMTS}}{{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:UR{GT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:UR{GT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:UR}{GT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.USER_HEADER_BLOCK_KEY,
                        "{3:{113:UR}{GT}{108:INTLPMTS}{121:5798a701-effe-43e5-8d14-eec27ea3d8ec}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TEXT_BLOCK_KEY,
                        "{4:}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TEXT_BLOCK_KEY,
                        "{4:"))},
                {getMTMessageText(Map.of(ConnectorConstants.TEXT_BLOCK_KEY,
                        "{4:\n" +
                            ":20:258158850\n" +
                            ":21:258158850\n" +
                            ":25:DD01100056869\n" +
                            ":28C:1/1\n" +
                            ":60F:D230930USD843686,20\n" +
                            ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                            ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                            ":62F:D230930USD846665,15\n" +
                            ":64:C231002USD334432401,27\n" +
                            "-"))},
                {getMTMessageText(Map.of(ConnectorConstants.TEXT_BLOCK_KEY,
                        "{4:\n" +
                                ":20:258158850\n" +
                                "{:21:258158850}\n" +
                                ":25:DD01100056869\n" +
                                ":28C:1/1\n" +
                                ":60F:D230930USD843686,20\n" +
                                ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                                ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                                ":62F:D230930USD846665,15\n" +
                                ":64:C231002USD334432401,27\n" +
                                "-}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TEXT_BLOCK_KEY,
                        "{4:\n" +
                                ":20:258158850\n" +
                                ":21:258158850\n" +
                                "}{:25:DD01100056869\n" +
                                ":28C:1/1\n" +
                                ":60F:D230930USD843686,20\n" +
                                ":61:2310011001RCD10,00ACHPGSGWGDNCTAHQM8\n" +
                                ":86:EREF/GSGWGDNCTAHQM8/PREF/RP/GS/CTFILERP0002/CTBA0003\n" +
                                ":62F:D230930USD846665,15\n" +
                                ":64:C231002USD334432401,27\n" +
                                "-}\n"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:{CHK:123456789ABC}{PDE:1348120811BANKFRPPAXXX2222123456"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:{CHK:123456789ABC}{PDE:1348120811BANKFRPPAXXX2222123456}}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:{CHK:123456789ABC{PDE:1348120811BANKFRPPAXXX2222123456}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:{CH{:123456789ABC}{PDE:1348120811BANKFRPPAXXX2222123456}}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:{CHK:123456789{}ABC}{PDE:1348120811BANKFRPPAXXX2222123456}}}"))},
                {getMTMessageText(Map.of(ConnectorConstants.TRAILER_BLOCK_KEY,
                        "{5:{CHK:12345678{}9ABC}{}{PDE:1348120811BANKFRPPAXXX2222123456}}}"))}
        };
    }

    @DataProvider(name = "parserBasicHeaderApplicationID")
    Object[][] parseBasicHeaderApplicationID() {
        return new Object[][] {
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("AppID", "F")))),
                        getBasicHeaderBlock(Map.of("AppID", "F", "ServiceID", "01",
                                "LTAddress", "GSCRUS30XXXX", "SessionNumber", "0000",
                                "SequenceNumber", "000000"))}

        };
    }

    @DataProvider(name = "parserInvalidBasicHeaderApplicationID")
    Object[][] parseInvalidBasicHeaderApplicationID() {
        return new Object[][] {
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("AppID", "", "ServiceID", "",
                                "LTAddress", "", "SessionNumber", "", "SequenceNumber", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("AppID", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("AppID", "CDGK"))))}

        };
    }

    @DataProvider(name = "parserBasicHeaderServiceID")
    Object[][] parseBasicHeaderServiceID() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("ServiceID", "01")))),
                        getBasicHeaderBlock(Map.of("AppID", "F", "ServiceID", "01",
                                "LTAddress", "GSCRUS30XXXX", "SessionNumber", "0000",
                                "SequenceNumber", "000000"))},
        };
    }

    @DataProvider(name = "parserInvalidBasicHeaderServiceID")
    Object[][] parseInvalidBasicHeaderServiceID() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("ServiceID", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("ServiceID", "0"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("ServiceID", "011"))))},
        };
    }

    @DataProvider(name = "parserBasicHeaderLTAddress")
    Object[][] parseBasicHeaderLTAddress() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("LTAddress", "GSCRUS30XXXX")))),
                        getBasicHeaderBlock(Map.of("AppID", "F", "ServiceID", "01",
                                "LTAddress", "GSCRUS30XXXX", "SessionNumber", "0000",
                                "SequenceNumber", "000000"))}
        };
    }

    @DataProvider(name = "parserInvalidBasicHeaderLTAddress")
    Object[][] parseInvalidBasicHeaderLTAddress() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("LTAddress", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("LTAddress", "GSCRUS30"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("LTAddress", "GSCRUS30XXXX324"))))}
        };
    }

    @DataProvider(name = "parserBasicHeaderSessionNumber")
    Object[][] parseBasicHeaderSessionNumber() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SessionNumber", "0000")))),
                        getBasicHeaderBlock(Map.of("AppID", "F", "ServiceID", "01",
                                "LTAddress", "GSCRUS30XXXX", "SessionNumber", "0000",
                                "SequenceNumber", "000000"))}
        };
    }

    @DataProvider(name = "parserInvalidBasicHeaderSessionNumber")
    Object[][] parseInvalidBasicHeaderSessionNumber() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SessionNumber", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SessionNumber", "12"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SessionNumber", "243212"))))}
        };
    }

    @DataProvider(name = "parserBasicHeaderSequenceNumber")
    Object[][] parseBasicHeaderSequenceNumber() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SequenceNumber", "000000")))),
                        getBasicHeaderBlock(Map.of("AppID", "F", "ServiceID", "01",
                                "LTAddress", "GSCRUS30XXXX", "SessionNumber", "0000",
                                "SequenceNumber", "000000"))}
        };
    }

    @DataProvider(name = "parserInvalidBasicHeaderSequenceNumber")
    Object[][] parseInvalidBasicHeaderSequenceNumber() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SequenceNumber", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SequenceNumber", "1290"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY,
                        getBasicHeaderBlockText(Map.of("SequenceNumber", "32119012"))))}
        };
    }

    @DataProvider(name = "parserApplicationHeaderInputOutputID")
    Object[][] parseApplicationHeaderInputOutputID() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "Priority", "U",
                                "DeliveryMonitor", "3", "ObsolescencePeriod", "003"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128", "Priority", "N"))}
        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderInputOutputID")
    Object[][] parseInvalidApplicationHeaderInputOutputID() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "9"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "KT"))))},
        };
    }

    @DataProvider(name = "parserApplicationHeaderMessageType")
    Object[][] parseApplicationHeaderMessageType() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "MessageType", "103")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "Priority", "U",
                                "DeliveryMonitor", "3", "ObsolescencePeriod", "003"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("MessageType", "940")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128", "Priority", "N"))}

        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderMessageType")
    Object[][] parseInvalidApplicationHeaderMessageType() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "MessageType", "10"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "MessageType", "94"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "MessageType", "1033"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "MessageType", "1033"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "MessageType", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "MessageType", ""))))}

        };
    }

    @DataProvider(name = "parserApplicationHeaderPriority")
    Object[][] parserApplicationHeaderPriority() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "Priority", "U")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "Priority", "U",
                                "DeliveryMonitor", "3", "ObsolescencePeriod", "003"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "Priority", "N")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128", "Priority", "N"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "Priority", "")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "DeliveryMonitor", "3",
                                "ObsolescencePeriod", "003"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "Priority", "")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128"))}

        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderPriority")
    Object[][] parserInvalidApplicationHeaderPriority() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "Priority", "5"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "Priority", "8"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "Priority", "512"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "Priority", "878"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "Priority", "NT"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "O", "Priority", "MP"))))}

        };
    }

    @DataProvider(name = "parserApplicationHeaderDestinationAddress")
    Object[][] parserApplicationHeaderDestinationAddress() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I",
                                "DestinationAddress", "GSCRUS30XXXX")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "Priority", "U",
                                "DeliveryMonitor", "3", "ObsolescencePeriod", "003"))}

        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderDestinationAddress")
    Object[][] parserInvalidApplicationHeaderDestinationAddress() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "DestinationAddress", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I",
                                "DestinationAddress", "GSCRUS30XXXX1PTX"))))}

        };
    }

    @DataProvider(name = "parserApplicationHeaderDeliveryMonitor")
    Object[][] parserApplicationHeaderDeliveryMonitor() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "DeliveryMonitor", "3")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "Priority", "U",
                                "DeliveryMonitor", "3", "ObsolescencePeriod", "003"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "DeliveryMonitor", "")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "Priority", "U", "ObsolescencePeriod", "003"))}
        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderDeliveryMonitor")
    Object[][] parserInvalidApplicationHeaderDeliveryMonitor() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "DeliveryMonitor", "P"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "DeliveryMonitor", "DTP"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "DeliveryMonitor", "124"))))}
        };
    }

    @DataProvider(name = "parserApplicationHeaderObsolescencePeriod")
    Object[][] parserApplicationHeaderObsolescencePeriod() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "ObsolescencePeriod", "003")))),
                        getApplicationHeaderBlock(Map.of("IOID", "I", "MessageType", "103",
                                "DestinationAddress", "GSCRUS30XXXX", "Priority", "U",
                                "DeliveryMonitor", "3", "ObsolescencePeriod", "003"))},
        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderObsolescencePeriod")
    Object[][] parserInvalidApplicationHeaderObsolescencePeriod() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "ObsolescencePeriod", "32345"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("IOID", "I", "ObsolescencePeriod", "TYP"))))},
        };
    }

    @DataProvider(name = "parserApplicationHeaderInputTime")
    Object[][] parserApplicationHeaderInputTime() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("InputTime", "0400")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128", "Priority", "N"))},
        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderInputTime")
    Object[][] parserInvalidApplicationHeaderInputTime() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("InputTime", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("InputTime", "04"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("InputTime", "042343"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("InputTime", "AFIJ"))))}
        };
    }

    @DataProvider(name = "parserApplicationHeaderMessageInputReference")
    Object[][] parserApplicationHeaderMessageInputReference() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("MessageInputReference",
                                "190425GSCRUS30XXXX0000000000")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128", "Priority", "N"))}
        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderMessageInputReference")
    Object[][] parserInvalidApplicationHeaderMessageInputReference() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("MessageInputReference", ""))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("MessageInputReference",
                                "190425GSCRUS30"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("MessageInputReference",
                                "25GSCRUS30XXXX0000000000"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("MessageInputReference",
                                "190425GSCRUS30XXXX0000000000PQRT"))))}
        };
    }

    @DataProvider(name = "parserApplicationHeaderOutputDate")
    Object[][] parserApplicationHeaderOutputDate() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputDate", "240327")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128", "Priority", "N"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputDate", "")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputTime", "1128",
                                "Priority", "N"))}
        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderOutputDate")
    Object[][] parserInvalidApplicationHeaderOutputDate() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputDate", "240"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputDate", "24032712"))))},
        };
    }

    @DataProvider(name = "parserApplicationHeaderOutputTime")
    Object[][] parserApplicationHeaderOutputTime() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputTime", "1128")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "OutputTime", "1128", "Priority", "N"))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputTime", "")))),
                        getApplicationHeaderBlock(Map.of("IOID", "O", "MessageType", "940",
                                "InputTime", "0400", "MessageInputReference",
                                "190425GSCRUS30XXXX0000000000", "OutputDate", "240327"
                                , "Priority", "N"))}
        };
    }

    @DataProvider(name = "parserInvalidApplicationHeaderOutputTime")
    Object[][] parserInvalidApplicationHeaderOutputTime() {
        return new Object[][]{
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputTime", "11"))))},
                {getMTMessageMap(Map.of(ConnectorConstants.BASIC_HEADER_BLOCK_KEY, getBasicHeaderBlockText(Map.of()),
                        ConnectorConstants.APPLICATION_HEADER_BLOCK_KEY,
                        getApplicationHeaderBlockText(Map.of("OutputTime", "112878"))))},
        };
    }
}

