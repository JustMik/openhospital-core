ALTER TABLE OPERATION
ADD COLUMN OPE_FOR CHAR(1) DEFAULT '1' COMMENT "'1' = OPD/IPD, '2' = IPD only, '3' = OPD only" AFTER OPE_STAT;