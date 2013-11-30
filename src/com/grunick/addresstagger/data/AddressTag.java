package com.grunick.addresstagger.data;

public enum AddressTag {
	AA1,    /* Admin area level 1 - Country */
	AA2,    /* Admin area level 2 - State/Province */
	AA3,    /* Admin area level 3 - County */
	AA4,    /* Admin area level 4 - City */
	AA5,    /* Admin area level 5 - Neighborhood */
	PFX,    /* Prefix */
	PDR,    /* Pre-directional */
	SFX,    /* Suffix */
	SDR,    /* Post-directional */
	STR,    /* Base street name */
	NUM,    /* Street/House number */
	ZIP,    /* Postal Code */
	UNK,    /* Unknown token */
}
