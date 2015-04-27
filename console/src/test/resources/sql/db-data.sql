--
-- Dumping data for table `tag_type`
--

LOCK TABLES `tag_type` WRITE;
/*!40000 ALTER TABLE `tag_type` DISABLE KEYS */;
INSERT INTO `tag_type` (`id`, `type`) VALUES (2,'LOCATION'),(4,'MISC'),(3,'ORGANIZATION'),(1,'PERSON');
/*!40000 ALTER TABLE `tag_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `image_type`
--

LOCK TABLES `image_type` WRITE;
/*!40000 ALTER TABLE `image_type` DISABLE KEYS */;
INSERT INTO `image_type` (`id`, `type`) VALUES (3,'GIF'),(1,'JPEG'),(2,'PNG');
/*!40000 ALTER TABLE `image_type` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `channel` WRITE;
/*!40000 ALTER TABLE `channel` DISABLE KEYS */;
INSERT INTO `channel` (`id`, `canonicalName`, `title`, `url`, `scale`, `facebookId`, `twitterId`, `googlePlusId`, `description`, `alexaRank`, `active`, `version`) VALUES (1,'cnn.com','CNN','http://www.cnn.com/',3,'cnn','CNN','+cnn','News, weather, sports, and services including e-mail news alerts and downloadable audio/video reports.',79,0x01,0),(2,'foxnews.com','Fox News','http://www.foxnews.com/',3,'FoxNews','FoxNews','+FoxNews','Breaking News, Latest News and Current News from FoxNews.com. Breaking news and video. Latest Current News: U.S., World, Entertainment, Health, Business, Technology, Politics, Sports.',211,0x01,0),(3,'nbcnews.com','NBC News','http://www.nbcnews.com/',3,'NBCNews','NBCNews','+NBCNews','National Broadcasting Company.',519,0x01,0);
/*!40000 ALTER TABLE `channel` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `channel_picture`
--

LOCK TABLES `channel_picture` WRITE;
/*!40000 ALTER TABLE `channel_picture` DISABLE KEYS */;
INSERT INTO `channel_picture` (`channelId`, `typeId`, `picture`, `version`) VALUES (1,2,0x89504E470D0A1A0A0000000D49484452000000300000003008060000005702F987000000017352474200AECE1CE90000093D494441546805ED58797094E519FFED99DD244BB2B90821A19004087814B0803A109D521103945BE48ACA25329D11296D198B2D2DCA21556990566804CA0D420B1D4895E19829505166384AA308844A0249362CD91C9BDD64B3479FE7D9FDB29B0B98D93F1C66F69DF9B2EFF7BDCFFBBCCFF17B8E37AAAF527BF9F0100FF5432CBB881E51E0FBF660C403110F8469810884C23460D8DB231E08DB8461328878204C0386BDFDFE1EF0057A3DB50A50D1D3D908ACF9989EE7FC287B43F73C285DE89E7BCCB5F758A3F37D50694847AF0F1E8793645243A5D5B456840452A9D5F036B9005252A5D1D0BC497E555A2D7C5E2FED0B28C3742EA2E33D9DD08901404660BB29FB5848DEA3BCF36F6074AA8022BCC7D10875940E31031EA5E771187A67D17B1409CB4237C1BA7D171A2E7F8BD801FD91307922A2D2D3613F770ED6BD7F87A7CE0EB55EE7F7041DEA7535C3D8370B895327232A231DF55F7C89BB070EF9E94859AFCF0B352996F4F23444F7EB07EBEEBD881DFC2318B2B35051B0014DDFDD829AE84247EB375E0940802DEFB63B601AFC43A4BE3E1F899326806C10BA57E675C74EC2D3E040CEFEDDD0A574956F4913C723E1A763712D7F2E3CB5F550E9B4F09087A273B290B36F17F469697EBA09E3611E3B1A25B317C0536F17EE2AAD1A097979881BF6341ACE5F203E63607A621019642F1A6F94B6F60A7169AF00BB871E864CD24BE3D173E50AE81293E0AEAB43CDF1937014FFD77F985A43F0F0C07EFE3F48FFCDAF44F8DA2FCEA2F2E34D489E32190963F2D0FDD7BFC0CD25CB040EECFEE4575F16E1EBCE7E898A0D1F2379C64B4818F53CBA2FFB25FEB76829B446A3D8CFEB74C2E7F1C0E776C3EB6CF4CF3D01288AEAC13FAD8398ACEF23F93DB4C99C370299EBD789F0D683877035FF55D49C3881A81E3D607A72283D43601A32189A68230C3FE82958BFB36B37B93B13553B77C271ED1ABACE9A09D3D081620C75941EC6AC4CA1ABDAB60DC6FE7D50B5F5AF705CBD8A94A92F222EF729F138C7862A104BAC34C7977C63C376305A7B80887C6E0FA2D252D063F54A68F451A828FC04B62345E8B1FC6DC2F980762C2C9B36C3DBEC9283D862499327C1F9CD159417AC47F6FA02747F6B29EC93A64B2200C19205F2D4D5A36BFE2CD83E3F461EDB88CC0FDE47DAE23760FFEA82585D82B7DD491D7F082AC0D8A703BC0D2E24CF9E096346066A4F9D86ED7011B20B37429F94047B7131AC7BF6C1557A4B32027BABE1F2D7D090EB79B08DDCB61A2412FEAD7BF6A2E65FA7109F3B9C82761CAAB6EC134B0A1D05A2ABD282945933F0EDB499A83D7306F1CFE4C23C6E14AA0F1CF6E39C091F60041560EB13CEB45DA211FFFC289ABB51B56D07BABE922FC2DB8E1E45C9C24570DFAD0DB02585290EBC94A5385DCA20EBFA284DF27BCADC39B06CDC84B8A79F44EAC285A83D7E8AF0EC14324987945ED53A1D5217CC47C5477F22380E41FA92C5A83BF56FBF17989253E97D46300608779CCB0D39BD05AB8DA565B4DF87F8113F86BBC686B27756A3B9CA064D8C91706F10EC6B620CE409129ABD171892FBE93D61E473D0C4C5C1B26B0F62FAF6A5009E41EEF1F8A9685DC549807EE37F3202DAE4645876EC84313B9B32DE3C78EC763F9D2AC857E1DFF637A800F99F8B8E2E214120E1BA552E2ED7984C705EBF01C7E52BD09AA229C2BD7E3C5371E302C7432C2A3352993E29EF698B17A17AFF0152BC0A690B5F87A15F1FA12202491612A4F42575DE5C893397A512A973662396D2A618853C7ABFD121C5FDF5EE842D6D5453A5E614E82C2D453459D4943B0CE514A81A6334A2BA75F76F240554AC290DC7F5EB887DEC51C40E1A88CACD5BC47806CA74201E5C2CEF3782146C39DAD05C5D2D58D5A7A789209EFA7A726D26A21FCB81BBDE2181CE2D43CBC391AB0C9689AD4B9EA9F8F3463455569045E7A0E1C245D45FBA24986F21256FF328FF601D5C160BD216BC06FBD97368B87245ACCF6878901154800EE55CDD78E51A9C253760E8912195B7E6F80968E3CDC858B6948A9599AAAE93F27AA3E4769953B02B8771CCB0F5D53A2D9CE72F512ADD009DD92CA9D5F29742784928255E945F67F137B8BDEE8FD0127493A64E41F987EB0482B2CE06A1F6E25E23A8000716B70F750ED47CFE19CD2993E4CF8465EB36B8AC5698478E44CEA14F91FAB3B948A07497382E8FD2E528A44C9B005D5A3751420A107B8078A963637167F30E386E942065C674B8AB6DB0FD93F8F23A29A9C4898A6A8D75E7A752D092A74F93CA5B5DF41934D46FF9DCCD0C0BBFFC01C8B55526A80031E6A0541BF472B0B3AC0C71C387C14C2DC1F579E4DE8B1711FBC823E8B9E277E8B3F513F4DE5A88DE5B0AD1879EE85EBD047E2DDD279DC27377AD1DB7D7FC41CE4C5BF4062C9B0A69EEF7B4B40AAC289DD754552DCD1AAFA5CE9F87AAEDDBC9D30D50C7C48AB2E20D96AF8311AC03BCC85EA0206C2AAF42E9D2B7904DC275A37CAEA32256BA7C05F454DC4C839F80A64B1782170386F75038E8F5540F1C70DDAE202898FD3021D7AB28CFDB0E1F85EDF87198478C80B15F0E4ADF59455EAE27C1390513174AAD5A52C2BAFF2012A74CA482F60CAA8F1C21BA955473AC50190D7C8A3FE3B117DA7842D5E17FA789B1878A4EE28BE33A69E61A5A2020CC033524EED95C248CCE43F3DDBBF8FA85B1709555529BD14C2DF140F43FFC3781C7CDB77F4B904B45C6CF97501CD951FCDC1834967C276C62073D8EFE4507A90E34E0E6F2DF43DFBD1BD2DF7C131E2A8E97870EA7EA7DA75D3BDD5E01D690DD45423D483B2D27B7F953BA6A35CADF2B9062C71E6263A4BC968FCCD5AB5B5196AD790FE56B3F828AEE0C0C106E22BB2E7805BD56BDDB9A6EED5A94AF292074105D1B24B55720B09571C741AD5C688C548482171A83C0470944A125CEDC2AD49D3E83BA93A7890BE57AFAD6B2469719D3F0A19400C6404B01CEB0AAF9C751691B94EACDF0F035BBD1E5D9A7601EFD02B888D61C3B81EA4345A22057FDB6A3530598505142AE948DD4F310837657CA508E9426BD84694EC7A29CE24DA12158120FB9A191157D2E37595E2B2D05E5573F978079B9A5E14B104BEDA763CBF34B001D2167B60EE290059ECA266E1D6833F7FD627666D2D9A042A8E12B24B718A187C99C6A200724CDD9306A035D4B794EC2CB39CC53E854C1B5103A65ADEDD1F7544088035651FA9EB60CDABDB3F03C947DA17316509602D654E6F2555EFCB30EE85AF153E8E9B73DA842161F86694481EFDB4B110F443C10A60522100AD380616F8F78206C1386C920E281300D18F6F6FF03D7E6E77BF97532400000000049454E44AE426082,1),(2,2,0x89504E470D0A1A0A0000000D49484452000000320000003208060000001E3F88B1000000017352474200AECE1CE9000015554944415468058D5A09905D5599FEEEFAF6D7E9740809D908980508108411260AA5121085024AA981386024808A22A303E8E052156B06C585948E5888A388E536A0326C3A8C30034620849048424CCC0284D0599AA493EE7EFDD6BBCDF79DFB6E3A66089993BA7DB773CFF997EF5F5FACBE39672678AB61A5AF1DC731B3C23084AEFD5C0E9EEBC28685248E115B806DDB489204ED76804ED0411445E6BD65710E62F3BDE779C8F15B8BEBC6FCCEB2D275354743EBEBD03B8D84EB9B33D77DB3B3F6D77093F46C6EDEECCFE15EDBDD8D49B9612223248E126E9E6E6A9E75C5A46B31AA231B7A26C625185D8B7131903161BEEF129A7D73E8B9BB3CDC74F2A1AFC7EE2D72AACDD0E598E4C0A61475D611F19FD6D0A1793A32420CD171FACCB66CB8D4A08E8C40DBB620C6B58E788F781D85D404F7326B8A89C349728C4473E51E72FF7F6E0DFD22325B909B5B0E99E0619E91808C11111526B1612625D6A6B6424D30F31DD783ADEF24180E33473C706D3D8B628A85DF1B01F09B98CFF8E5D85C7335F647DFA4FB105A1946C75E1F7A9512963D35447721922D9449D41064B04D46B9BF509EAE1F1BF8084222328AC85C77683D6950B0D2A1FB0C7E29C329143382A5BAF4B984A123659436925E640B1F7ACE8C297BAE05DD2E4106420937A296B478B67162A5866A56E67CC7F109A9D40EB48E4598A544A4AB8A0119B88698D01E5A4BE73106BA1A4C3F39F037A3FF88D0E2D2FC688CF3747111D5F5364EBAE11823876C48626C32EE389E5927233093644C1B926D4891B69DDA4F2A100980D0E48B43193AC0459732DDFF3F1819FB2C23E2E0850FBE4E88AD144EA996355F129606052BDD27C998731091D9A15D3248194D77EFB33DC98D21E450FC64F7F4A2A9FA0E777665DC5180389FC0CD5162A036BC3C63878550428E62F84E8906E120085A88C3361CCB4718A458F769E0397F3C5A7C675B2E12CEB79C16DA9CDFCE7B705B642E0E90780E1A3C075E07B96A9EEFF97D810260DC917064F88EE2168F88D7816C8AE76C1C9111E282F826C5B48520080C4C242592048F42084B790C3477A36137E0154ADC380F2717C02F74D089C81459F75D0BA552914B500CB93C5DAC8371851E7476EE47DDA133A12082B0816AC94225C9C31A6DA3B7E4A15D4B5DB9B429B79D69CAF77DE8C8EC494A3862400C49082307426195A229F756D066D4AE8F8E6068742F5AA850230E9AA3A3483A5434DF552696501897472EEF20F40BD8D4FF0A2135027865B8F90A7AAB15C48D36F60D0E03ED8800CFA3D05B4033F439CFC7FEC17D145387F34BE8EBCB43D940063B09311B62C084003E38A2FB25A2A90D8BE94881ECC454A95C65806B177F188BAF5E04A7DD44D8EE209F67648E1BC857FA70CB1796E1A1C79EC1F0C8306E5A72362EBDF8228CEFAB903417FFF9C4722CFBE61D7863CF105E5EF33C8A450AC88DF020E77FFAA6A5B8F082F7E09EEFDC87A03E8A4A5F01E72DB915BB76EDC2F0F0B0D182EFE7D0E974D31FA245E4695813E7BF778CC5F4D95FFD8DA9529F780D981B85AD36F28502DA9D1ABEF489EBF0E9EB17C3257C805CEAD89280A2F170FBB7EEC457BEF90D3CFFE24A9C78CCD1B48B36F7233428678BAE588EF6AAC51FC594295370C7576F23FCEA68A384A3A6CFC3FAB5AB3089B0F2E9AE9F5DB312EFBA6809264C9A849E9E1E036D3191398E769BD0251A348E6823318D2A0E422461846AA5079571E3607B2EE8198DCB8BB850C76920A4018B89763BE63C076B9F7B12271C33198EB12D1F9FFBFC37F0EACB7B093DB24DD1DD73CF3D78F4F13F60E5EA8D7C56E4B336FA37FC11D308319F26D9B1135CFAB1CF63E6AC59D4761E8D46C3D88812CE0C66825CE6A49438899DC31EB663219FF38CEBEC7442349A4D33975E951A6A52CA2E091C81E7CFA4B48F45B5F7040C0D069835F358BE21D574B7EBB66CC4F77EFE4BFCE481FBC92B85D21C45D909F0C0FDF7E1FD8BAF47C3B3A89110B97299999B72AE18DFBEEBC7A8750A460B990B3636411BC9EC24B311D9099D8F02DE5B0CE64AC65B708162B188901E2868A6B1C0258348EA98DCEBE137F7FF003105B2F5D55771C1B9EFA34BA5BFA24742C4D8C240E79527A0D9B54CDB88BC8379D3A76064D300FEFDEE7BB1E4BA8F901525A10946F78FE0B6A577A0F76DA7D0C1D44D5620E29501C8285CD73734095A59603E62F61B39FCD8A6D708AA24DAA67B15C219C8C47FC278CA73B5ECE3924BCE23DC7C6C79759B61D6682CF208B90E6C7AAF66AD813EBACC88B2532C083AAC699C3ADE7DD119B8F777BFC787AF590C8629C62CE0A9679F4358EDE1D26DE3EEDBB40B23443222379CE5653941AEDD32F0A28D28B739FC61C71E027AA66A2F8D94BEDE0E12F4E4ABB49B0ED9015AA1F44A82833CA5CF753A2E4607093F0EDA2BF222BC19A3982FA311D250F9BDC5D73E83639D4EE2A9879FC677BFFE391248783540E30D29940F60C149F3E0B6D33822BB30318CD2C90A3B3DCB924C41EC881AF1921E140B6D725EC7B87289AE96693809F318B824758B2E70CDFA17F1DD7BFE031E0D54DA9A356B364E9F3FC36830B173086B7B51DBBE89F711237748574E63E63A3B070671C367AEC229734F22532D948BE49C4B8454CBDDDFFF22669F7C31A6CEA6ED9150115DAD5631CA78A5EBB4CA1CCB4A9CF2B4D94B399314BDF9E1790534EAFBE13232474C330A852283E128DEB3603E4E7BFBC9B019B7B6EFDC811BBF7807FA6BA3D8F2FA6E2C5FBE1AD72BC6A0207D60DAB4A351EE29E3F22BAEC05124C6122E7D1B572CFA18BE7FE7B7E94C487D94C3AF7FF128E69D329B08B07054CF78D8BE85156BFE7CC0DD2A16EA10BC52CF4506F9408C72C5B71EADB086026DA0C0F851AC94D1A671474C412206B1368B20B9E1729E35387154A0332894AAE89D3815175DBE187BC8641AB042DCFCD9EB307BDA04DE8674D7166EB8F5567CFCA38B98AA108E6437A4F75E440F76EFCF1E44C177D1AC035FBEF163A650D326DABBC33AC6E35E4A75EAF498DA530CE970AA534F582A7E0E7714CAE3D1196A70F10A63099335CAD92B5431A5B717C78C9B82D7F6BC8117366CC48A9736A14C6DD9B4995C651CF68C0CE22B77DD2D12297CE65FF95EEC78632FFEE7D93FE0439FF8329E5CBD1A377DF253A8F5EFC6963DC3587AC7D7B169B08D077EF724DE77FE02040CAE83DB8750983A036BD6AC31D17CFCF8F168D2FDCB8B2A400A662A1134AC29675D42651D7EC45609BBD7AF22916504B48189734E4444B88D6C5889FACE57E9BD2A40811A29310326A3B69C1AE7C69CEB1EDD8768F34B885BF45C33E630D055D0DAFA0273A83EF4CD3F0ECDB68BC6BA75C4CA1EF4CC9CCF0CB917F34E9A80B9338EC2F64DBBF0CC1F9EC084534E336810853278D986A2B9EC441093471363470C88BB5FDB80E6C8660C0FACC1BE5D7F62F23884813DDBF0C31F2DE3627BB06FFB46BCB4FC310CEF5A8D78701DC27D3C76AE40B47D156EB9F2FDD4E60E24C10016BEE35404232C0746F720A96DC0E5179E8E7034601AF2081D480DE7FFED3C3C73FF3FE385877E809FFEEBED58FEFB1F63E3D6E74D8E25E2051FB9609DC584883F3830D2FDA6D5DE61CF5E2FBCA041FFCEE49052B9EFF62F20DA314207D4A183B151ABD7B19F31A262313D8F0924473915F32F37C68A551B38A345AF6CE3ACD34E80DDBB1F4981350703E37BCE7817827D433879FA24C6161BA7FCCDA938E9F4D38CD379F9959708610BC74F9B8EC7EEFA12068643B4ED0253158602C6B2884C7826DB50A3425525D3FCACE695EADE74A8ABC13235A19B2D72C229A7BD030B29BDAC7FD0F61394FA7C46ED269E5BBF15975D733361D883BC93C3B6BD6D6CF8CB56CC3CF9249C79FAE9B8ACFF75A58D0C3B166631612C9488EF2A61B4F9655CFC8E058C3931D6FFE5159C7CE239F8F88D37E06DF3E6E3FE071FC4F82A619BB4303058670940C1167C0C365BE825B49424C9A31C316924F0113111E45FCA0D28153CDCB56C296A838326E2375B431865EEE4335E1C5BA962D90D4B70DB751FC4DF5FFA4E849D7EAC5AF767B3D1F124FCAC39279011B1029C3A67163E78C942320C3CB3E2693446182529B47973E720EA8CE0B24BDF8F8DCC9E576F7E03FB9B35E66674C9E55E6A264193496C2E617CE2339B5987A3CCC3A22B7BABC32BD1903DB647C9CAAF1E7C1C761CE2F8B74DC725179C67D49C2BF928B2007218D567CE9881451FB91C8BAEFC3B9C77F639A83A153CF1FC8BF45CC09CD9D3317FCEF1B4171B5B5F7E8506EFE08317BD97EFDA58B76B18D77E7519465B8425F34C9BB05CF8EE33F0A33BBF8A6F7DE916E3B18220429E395A681A78364A392594DDC604A14B214BCE873F024A89AD5C04C4657FFF4EFCF6C967595C753069F2D15C94D2EDE4D1621104AB85ADAF6CC65D3FFE057EFEE07FE1BEDF3E8A36EDE1578F2F671548F5738B73CE3D1BABC8D8AEBD439C9FE0E28567A3CEE0F833A6293B5838557AA7E1B63B7E80152FAEA6FE0BC606FFE1CA7311EF63BD42A80E05A328450EF24C89EA6E9BC2F54D304C03E26122FA8148CF050A4CA342BA61749AB8FAD6AF99E2A84D69149509337FCAF78CA30DE5D1BF6D3F6EBAF6665CFDA1ABF19D7FF9216133830D8A3286F6B274259EE420B6EDDA475BDA88161F387684FDF500F5DDC378E4AEDB910CBFC608DFC68233DF899FFCDB6F187009670AA8A37A88F011C16A46A81B9930D7530C3109355175641B513919937B65309E8F7D6D07DFBBF70193F8526CC8F5E431526318E658B0F04CEC8F77A2156F45545B8B4F5D7816EDA180E756AF613D4E6248D80BAF6CC3CA8D9B19560938DEBFF4A717E94A43F45428284AF9E6CF7E8299F230967CF443C8D31B3DB5EA5594A64E26E45AF0D479D167B493324BEF80C558E66D8FC8885A3F4A114CA02B8D835BAEE09FBE76279BCFCC2908974AD3C55145763EE226151DA0C4F0EAC56566FE050CC7C3B07265AC5CB7B6DBCAF1F0D8D3CFE0E1C79F60B62A822CAC5FBF01EE8CE3F081CF2EC5A69DFBE01459E3F89434B3E0D7B76DC3A2CF7DCBD4EA4A48639F892ABF5360EC50C0EACE6890095833DE7B65A220638A273DEC5E2BD898DF32E8198E9D3F0D9DE13C36ECECC7DE3A19609361D651933177D231D85D1BA0EADBE86377846522EC1CD31D163C01A5B7B6BF1F832C92CA4CFBDF396732729687DFD245275E827366CDE5741F4F6FDE42795868D46AECC2B43173FA54CCA2D3D8BB7B37FEF4C26A148E9D847CB3886ABEC11A86D52A8BAAC42E217603862AF6D18C2B215A8E3DF7AA038C88783192454C9778DCC966C1DAD7D732FB4DD022948AC46AC0FA5DDEBB9D6BC36FB195C38432A45F6754C248A30E87F9963F81FE7E944D397AA021AE536A84845C4BE689A468A34C466B6A6A50B24A393C97899F9C33D74E18B92DDA819EDBBB8730EFD4B7A36794B5482EC2E4846DA3846BD1766C87ED25B65C355C112E23D2A171F059AD4E8F4D85527D80865E402E60D3C1A777A1A13739DD23B48A8458876D9FDE4ECC29314A0471F398A918647E3F75C71ED4E7CDC4784670D498AD4EEE45302E07B7D1427DCB2E66BECCC7E262170D693AAEB43CA2CD881CE5520E1B804987855CD440919A0FC9B0C5ECC18AF3282A93D0CF161CAE70AAAF14A6B886F9F549F71619946DB4DC10C30D760ED9681B25A44256784CA69127A185D8C78053637A4DE91619A0F6D510393D38FEDE6FA0B4613BFA1FFD35265EF349166479BCFCFD9FA2E73357633A93CBDA8AD518EDADA276E17568F4A635B988511B543F22B9ACBC24E780C966A9CEF5278F3056E5D1492AA8D341B82C230A4185314D5F11261CAE609469410F749D3D936A7B585B376817FB3B097D37FBBC2C8B1D1A5E9D29491CB34FAB7E2D25D34FCF35F1E469702AD311311D5FFFF01F31E7CCF9187A7E398299733161C6D1709FDE82DD7373D878FDCD38F1A1BB314CAF33448AA3561AD83CEE97E8572BC51DB9670A32126429648FAD56753B13162E9C6D042D08D28B886C5A0A93307EA257528461243D2B9974D12A4CC01E7A094129EA70611656AC0AD416A05AF99C9B1718D543D6D7D5E38E636B681792871FC2F853FBB0E21FBF8639CB6EC1C0CA1750FBE5AFE1BC6B01CA7FAE3206041878E4BFB183F9931BB073D86602C416904F28B91494A0218644473112A16506CE8842A4F6983CC6CC00A23CED88C1D4A65035AC99E72F31C6AE1BE541FAAD4F1A917A736C4FBEC620F8C8EF1FC044264531DFD598FFAB8D5A640785CB31B025D8CF4E4B891E2B4749B2AFAE4C1411EBEF66BD833E6E3CA40C98E56C89CC0FB39534CE65679259674422947130494FA5AF762CE52DD19A5F01C8498DEDA4F3CE3F1F7D2D32900F318176DA24BCFC1E4299BD628F0C6A18BD64D092562406FEE5A184CC46D5ADE082F72D448F3785E5AD85614662B0C5A3199AEBA84D2AF5B2C34E3A789DD09ED811196ED0505955D206134666F58F0A3E854042A0CE4C7118497B1C43067FEE963D1A93D4997D3076CB63456F6966B486C9E8A35DB004A0765426841D9F997281DEB1C6FD28140ED79161F103FD481993920E39CC138F1E7B461D62D0690E6366E9685677369AF4F3658BA901B521E623BA4A9BBD2C5A3F07554C3A74626EC13E50C1489DC24B07D31131AB1CCB08A2CDF702285DB02381F09F49FD641E5AC32C4683675519B1168AA5377A2C6DE5F2B79A0E6395E7331B308180CFB24A4B8429DC3B9448E692C5A0AF204789E9D75A0D6D6906E759B22F7EA7919DCDCD417F4C2E74D0FDA197FA952B21648D01672F498786521A23686E215A0EECA16BA345BD10DD6A5F708881982F04279706AEDFFB624ED0E23E3346558209038FD98C9A4BF31BB2C42EC681C5B5D02143EFF44BD35B0D5B84E89F11087562D697E7942AB3BDD21AFDE075345F42B5D505E420DDACFEF82F5BCCE6BD70AA6766026B91200C10D28B68E1035D0B799743C6C14C8D5DFF352363CFD38F05A974504CF4805A5F084812BA620AC166E41775E6384428AA6EEDCCFD669CA592238F5A885A316433CF92DA8D46A40F2EAA4D08EB74105ADA4C43EBE82A2334BBE6126F3AB279992D08EB0615DC430CE9DE7C4AB4642D5D062EB39F6855A268CA74335736623096EE9512CAB291EA12812E0B7BFD7C4CF268A05C9C1FB32B969A6197C06C51AD708038CDEB0E09E1C0F3EC21CFD9B331216B7D419E303F0029EE4D412A24383C146BC48CC523A545F4A4F0669A27F5112694AE5E0AF746EA64C06280D2AF541619923119081A66D28F8DEABBC489B08C38AD935D0B12A968C7B89034B391FA10CEEA3ECBD631C810C4BBC6ECD0AD67FF73C8D28F99644C9F087A1AA48F0F4828BF300C68A198471A9834851211A35C54BF5E99F95A4187EE0EBACE88D0F3EE6B323FC6949E6B6444776FC6EEE59A39B45F662FF2AA9A9F42890A21E78C91E6DE089CEBEBCC04975C33DB3410A0066C455D122D46021AB84DF76BA46A16A723D09040E85D941D078C3B0733C01BA3DD9424CE358EE3AF893FF08EAF8DC7ECE2CB082C5D5EDC50C04247AA2D35E5BC1C4301E96BF23711ED433258B92A2B30F526EFDE6A18D10A76A9143455C69E116FA4C7399994CD5930CD0619D1C8DE678FB3B3F14A5D46B23982ACAE7598A86F68E0FE5DC34E61D77534DD7AE47F01C98F43052E2E2CEF0000000049454E44AE426082,1),(3,2,0x89504E470D0A1A0A0000000D49484452000000320000003208060000001E3F88B1000000017352474200AECE1CE900000C76494441546805ED590970944516FEFEC94C66924C0EC80924E482002258049443C183450E2102B2E802CA212AA8EB8108782DC882E8221A45175DC40211543C2A8B17A7222088201037820910EE04CC698E992473F47EDD33133299491665AD528AAEF4F4DFD7EBF7BDD7EFBDEE8E065DAAC045907417010605E11290DF9B262F69E492467E23095C345B4BFF7F1190602812A524656796B291A149E35F4BE6007E33091BF3197ED4A9AAEBA7857B4C83A65FF979E14084930C16E3ED556FA26F6830CC3535B01802B19FCD19136640545591B53A74EB968667E6AF8231300E369B804E578B23F9BB3165CA24F6A71010815F40D22E28B22B4D14E1C0B6F5D016BD084BE14F4000354070818106442D5C80D6FD6F47FF5EED3177CE4B78E5E56FD84D6D916727812625B544FF01E1B8E186DE6C4B55EDBF16CB8501719660C55BAFE3AA0FD6C05251C55DE4DE46921B72AAAFADC5E9471E823936052F6766233CCCE0C5A7CDE644C78E71D0E9F762E6AC050412EED5FF4B2A1768EC4E5CDB221496B3C5DE2024073A1DEA9C023D353BF28ED420C8E4BB94C1A0434E4E21C1F4A4167FFE257CFB8C3D7F1B113452512E396496FB997B430B42286DA2A4A1261A2CA119F4A8CE3F8A639670180C01903BB1B1290404485AC1CC7406CE7C96D251C864E6E028E6065A7675F8FD3D3F20CE52F41A7023321F198FCB8C56684E3B726B023173E506945B6BFC12568DD4884D1F88B94FF5C7FE7D27F1E2A2ED08347A3326C1D5D55661EAE47BD03D63026A82A3A077D8602C3E8847273E82625B2DC1189B5EC3DDF3BF81882ADC3A7912DE99D807D6E553E1D0EB29330D1DB9D8FAE177A1AAEFF5287D2E130141269FC584DD8ED0B4F6C839F813E6CCFE02D1D1413E631C3607FE34301DFF46021ECBA10BD72AD418CDD00ECBBFDD8927460F45F6A162B637CFAAEFC66DB89474AD3061C5BD37A16AD934D8835B400486329BE1086A81AACD4B11F2C3670819320092E9C649B6052727A2BCAC1AE6106F439763EBEA1C1835B2031E585788F5876967E455EE24B59BA8F5899F1662DEB2E5DC6DC71B93F6A9370F049578FA1F3311B0EE658890509FC95A70042C1FCE455CC640382D7EB658AD0D416DDAA0755C281CDC668D93ADC681969D12F0795E3941F86145E7C4B73551183EF816826918481B537259AE6FABA745D891161F0DDBC9FD1CE94FB534548ACFAC1523203AC6151CEAE77203329668A161888B0B879DAEB661723804AEEA118B0D676A09421ABC9FA4D3B0E544057AF7E9C14E3F826A30C58F181AF4CAE976326490C6E62B5135323004B5EB162376F278386B6898EE249C0E98D2BBA85A70B08101D2E5B53CFD75B57674BF2E0DFFDA5344593401849E209CF3AAAAAA39AD79569BEFD582F1F1866D08EC3D86DED1EAE1C1BBA4469C3FE5233289B6534317EA497607823A75F0D490941CCE18E9128620835111461CD64C74C74D0848CEE4F88CB470BCF5E6FBACF83A8A7AE2FC681E080DFDA3958BF16D741FE8A5D4EC4DA8D7100467F6E788B86D14B7B20B8C32F49494FAB5929223EA81D4D53A316C64673CB58387C800FF2C08EE84B62D42107060238E17E5BA3D403D399F0FFF54E43061612EC703D39FC14EBA3FDDFCBDD025F6647399ECF426A437C1B6E35DC45C77259CD56ECDD9EC084A6C5B3F2E212102D22E6412D496352612B5169B4F8094FD4E02BD3925125F8FEF0047650D268DBD9F8D47E444D9ED37F907C2E3C2901137C372FA73648E2BC08369CFC3B0EF5684DC7E1B4C772C86A82418C611AFA43720D8720C811DD3D8E720333604B74DA81FD2AA35B796C3C993AF13370D618C38404FD5C83604810A7B00966624E09A6EDB71C397B7E3CDB44DE8F07857EC3AB00F2971B104D3685DF70ABE4038B05D972BF1C92BC360DC3B098E822FE1A4BF17D5C7E1D8331D06DDDB30CF5B0B2DE67208ABD48E3B198261FD6411E2C68F8193D15EE3963144467A7AD1BA5504ECDC2EB61A3BE2BAB4C557F915D0E8953C496A6164720C568F7360DE89BB303B6715CAEBAA5161ADC6EBB95918913713EFED5C4A205C531E071A253F40CAB1EC85FB21F6CF809352A60FE5142E28A3158F1BCE9F0F42EC1D0BF384D130DEB200A282D198C18B5CF1EE518288689664D8D4B593D752D13166D8EC4E5CD6A1057655D213BA41282DD874D44222523A7E883B763D0CAB2300A10141D091A6CCC13A23C284094F1F7B0D4B325F2210DF03A62F10EEFF6BDA51C50AF43989D57325010518E0C8FE1B02C33E8379F6078C15B4855A1E2D0C21B06F5F85C83BC7C394D6AE7E8AE7A345B811FD0677C22265E49AB2858CC468ACB94387674FDE8DD7F23722D210490FE4BBAE9EDEF1BBD2C368D3239EE47C9D8E1F201AB4600F100F0B8D4B2E24B553B61FE28789304F990CFDC047B9FDAA60CFDE84A8F476302524369E84AE5DA2708AC71B415B815DC3D261C9B8BCCB2718F3CDFDA8E4AD3184926F2E19084618BD03AB67BC1F2002956552A59E21CD941AB79E4E6AE72998DA7C8790596F032131D01FDF81A88137FA4CBC73726F3CF8C5690C4E8AC687E303B1A8600A5ECAFB985A88525BC86742A3061BED37A0C2FBF4EC19E2074800B276334287F2C8D18CBBF31050F623B553BC13383215E6698FC1596985292AE2DC10F75776A9132F5CDF06BDD237E2D69D53504ACF66A62D9C4FAA7058F06087D1F8F2A32D5CD2CFB9CFE7CE2E8D4394E0F88F1B115FF800E34665E3A8D1F4BAD2AEE8427531E9D02E7F0E9AB9B5D7D87D453FE2EE5DB370CA5A8EA0F3042049D6F27566687C2F8C2CBB1E8307F5E72E48F5A22B2BFEEFECEA65A402EFAD7903C3BB6B30E83D6EF67CF61BA94A4D3A79184CBE9315D7BE3F653B83ACFC4D08E3D98C5628D73EEF145A1982E3EB8FE0A11953FD8290849A0022E5C044CD28F7AB719B29BD34DC898D9971A8297469EEB1EC5757634F00332152E3F5D5DDAB3EF823A9E8EBAFCF9ED67365114F184E719A0D5C5B635C6AE282E50B44F95D79CCA024E925E862988FF1BB15CBC2732B4886357904A1F4C549184D69A8655003E43B16EFF2A1465456F058A1497729135DA69CAF45B32C7235A95F1934A5C092D87E0CF549D21627580D63AE40F7EEFD515AFA338E1E3D4A4CBEF6D750C42E1A221F8505FB5C44452D5AF12E214FAB2347F016C8D293B3B2D672CC51C4C747E1D8B1E3A8B1E6F239EB10060DEC4B3A16F4BDE60A6CDFB6558D01ACB86BF26835F7F9857FABA721692D5ECCA02A854710B2DCB0E10B579D732A2B2BB179F35A2C5BB6127BF66C423E1FF466CEE4B98BD76FDF24FFF5D63073984C050585A41E2842C33AAAFAB0616354C98734316BD61CD737C756545488BCDC4342D3B5E7D84E222DAD17C3761BD1A7CF603566EBD6AFE53E159326FD55D5E7CF5F249CF23CAFC58AE4E41EEC0B55ED73E62C1099994BD4F7FAF59BC5B871F7A8EF848474554E9BF684A2D3BBF740CE4DF4E659FDFBB02108D5E00262E3BB665959B908347650843C40962F5F2D72720E88830773C5A0417F567DD0E2483C9E3989399939A11E881CB07BF75E9191E112C4BC79CFAB39AFBEBA54AC58B15A319795F5A9B0582C222EEE0AD5376AD47891977758E452405208870E1D51E09F7CE2EFAAEE25780FFF3E8D9C289394C88913A7B88055D53D40A484BFFFFE0761B5D688871F7E5CF5C9C5A0A59C93520320575F3D448D29292955CC7880ECDD9B4D61E491B120111BDB458D292C3CA3CA93274FAB72C2847B49375631BF6AD51AD5B672E5BBAC879E5BCB0DC4D746C8954C417CDE69DB361E24EE6A70FFF6EB370C5DBB7686C964445494EB74BB64C93242C9F77B67C83F7A1A6DE2BB213C3C8C770F3A5E666E2DA4A75F814E9D78E4A713397BF63FD8B66D27EFF6B198316336EDAE358A8A8AB17CF93FB96A3002E948C68E1D8DB56B3FE33B713FB6557AF1242B4D02311A79EAE54B5F6A6A328DF904CCE61035F9EC991FF98CE372A94BDF7807D3A73FA95ED4CF9E2DA25729C788E172A13AE8DDAF22E1612128283885C4C41EA8AEAE86A4ABE3736A6949198D99462B5D2B3DDB9C39CFA9BBF9C28573B163C72E65E0000545875274F63B701720236308B66CD94EFA2E37AE1872FFF871BF365CDDBB3372F34EA2B884B744153FAA90D63E95FF1AB84C4954329295B51516AB64A4129D3B77C57DF74DE07DC38EB97317735E35225B8660E8D06BF1FE079B61E14D50C6E7B4F609D4B4916FBDA9F57456BFB38E4028347AA2EBAEBD0A5BBEDA835E3DBBE09B5D87DD2CD660CC5F6EC28001D7223B3B079999CF727C2AB3BBDB5DF802911DEA16C6E3BAE7A156BA47F9D6EBF54F1AA93119FC98E4780252D4E58B3AEF10B434D6193B78EF3F4787314909C61324C90D1F20EA93A4230FA2EA8C27E3949B5BC1B826AFDEEAE994DA680442CEF70FA49EF21FE7A3491BF9E34070717A09C8EF4D639734724923BF91042E9AADF55F47EE814C393C3D1A0000000049454E44AE426082,1);
/*!40000 ALTER TABLE `channel_picture` ENABLE KEYS */;
UNLOCK TABLES;