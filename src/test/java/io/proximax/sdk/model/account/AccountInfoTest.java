/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.proximax.sdk.model.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.proximax.core.utils.Base32Encoder;
import io.proximax.core.utils.HexEncoder;
import io.proximax.sdk.gen.model.AccountDTO;
import io.proximax.sdk.gen.model.AccountInfoDTO;
import io.proximax.sdk.gen.model.MosaicDTO;
import io.proximax.sdk.model.mosaic.Mosaic;
import io.proximax.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.proximax.sdk.model.network.NetworkType;

class AccountInfoTest {

    @Test
    void shouldCreateAccountInfoViaConstructor() {
        List<Mosaic> mosaics =  Arrays.asList(NetworkCurrencyMosaic.TEN);
        AccountInfo accountInfo = new AccountInfo(Address.createFromRawAddress("VD3YBIRQMWXS6HHUJBXFZJ5662BJPXOTKOOV6HDO"),
                new BigInteger("260634"),
                "B0E490BFA19E793E61BCCDDFE393F3639DFD4D2DBAE9D566CDEE6D29D9690211",
                new BigInteger("260634"),
                mosaics,
                "429642486699C64611A0EB102BA2563050398F17A3357CFBACA1B3C6A9E8C538");

        assertEquals(Address.createFromRawAddress("VD3YBIRQMWXS6HHUJBXFZJ5662BJPXOTKOOV6HDO"), accountInfo.getAddress());
        assertEquals(new BigInteger("260634"), accountInfo.getAddressHeight());
        assertEquals("B0E490BFA19E793E61BCCDDFE393F3639DFD4D2DBAE9D566CDEE6D29D9690211", accountInfo.getPublicKey());
        assertEquals(new BigInteger("260634"), accountInfo.getPublicKeyHeight());
        assertEquals(mosaics, accountInfo.getMosaics());
        assertEquals("429642486699C64611A0EB102BA2563050398F17A3357CFBACA1B3C6A9E8C538", accountInfo.getLinkedAccountKey());
        assertEquals(PublicAccount.createFromPublicKey("B0E490BFA19E793E61BCCDDFE393F3639DFD4D2DBAE9D566CDEE6D29D9690211", NetworkType.TEST_NET), accountInfo.getPublicAccount());

    }
    
    @Test
    void fromDto() {
       ArrayList<Integer> uint = new ArrayList<>();
       uint.add(10);
       uint.add(0);

       MosaicDTO mosaicDto = new MosaicDTO();
       mosaicDto.setAmount(uint);
       mosaicDto.setId(uint);

       AccountDTO accountDto = new AccountDTO();
       accountDto.setAddress("cf893ffcc47c33e7f68ab1db56365c156b0736824a0c1e273f9e00b8df8f01eb");
       accountDto.setAddressHeight(uint);
       accountDto.setPublicKeyHeight(uint);
       accountDto.setMosaics(Arrays.asList(mosaicDto));
       accountDto.setLinkedAccountKey("cf893ffcc47c33e7f68ab1db56365c156b0736824a0c1e273f9e00b8df8f01ea");

       AccountInfoDTO dto = new AccountInfoDTO();
       dto.setAccount(accountDto);

       AccountInfo accountInfo = AccountInfo.fromDto(dto);
       // assertions
       assertEquals("cf893ffcc47c33e7f68ab1db56365c156b0736824a0c1e273f9e00b8df8f01eb", HexEncoder.getString(Base32Encoder.getBytes(accountInfo.getAddress().plain())));
       assertEquals("cf893ffcc47c33e7f68ab1db56365c156b0736824a0c1e273f9e00b8df8f01ea", accountInfo.getLinkedAccountKey());
       assertEquals(BigInteger.TEN, accountInfo.getAddressHeight());
       assertEquals(BigInteger.TEN, accountInfo.getPublicKeyHeight());
       assertEquals(1, accountInfo.getMosaics().size());
       assertEquals(BigInteger.TEN, accountInfo.getMosaics().get(0).getAmount());
       assertEquals(BigInteger.TEN, accountInfo.getMosaics().get(0).getId().getId());
    }
}