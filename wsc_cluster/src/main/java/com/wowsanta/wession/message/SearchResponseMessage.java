package com.wowsanta.wession.message;

import java.util.List;

import com.wowsanta.wession.session.Wession;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SearchResponseMessage extends WessionMessage {
	private static final long serialVersionUID = -8818613579194880957L;

	MessageType messageType = MessageType.SEARCH;
	ResultType resultType;

	private int totalResults;
	private int itemsPerPage;
	private int startIndex;
	private List<Wession> resources;
	
}
