package com.sniper.springmvc.rome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom2.Element;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.impl.CopyFromHelper;
import com.rometools.rome.feed.impl.ObjectBean;
import com.rometools.rome.feed.module.DCModule;
import com.rometools.rome.feed.module.DCModuleImpl;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.module.SyModule;
import com.rometools.rome.feed.module.SyModuleImpl;
import com.rometools.rome.feed.module.impl.ModuleUtils;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndCategoryImpl;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEnclosureImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.feed.synd.SyndPerson;
import com.rometools.utils.Dates;
import com.rometools.utils.Lists;
import com.rometools.utils.Strings;

public class SniperEntryImpl implements Serializable, SyndEntry {

	private static final long serialVersionUID = 1L;

	private static final CopyFromHelper COPY_FROM_HELPER;

	private final ObjectBean objBean;

	private String type;
	private String subtitle;
	private String uri;
	private String link;
	private String comments;
	private Date updatedDate;
	private SyndContent title;
	private SyndContent description;
	private List<SyndLink> links;
	private List<SyndContent> contents; // deprecated by Atom 1.0
	private List<Module> modules;
	private List<SyndEnclosure> enclosures;
	private List<SyndPerson> authors;
	private List<SyndPerson> contributors;
	private SyndFeed source;
	private List<Element> foreignMarkup;

	// com.rometools.rome.feed.atom.Entry or com.rometools.rome.feed.rss.Item
	private Object wireEntry;

	// ISSUE: some converters assume this is never null
	private List<SyndCategory> categories = new ArrayList<SyndCategory>();

	private static final Set<String> IGNORE_PROPERTIES = new HashSet<String>();

	/**
	 * Unmodifiable Set containing the convenience properties of this class.
	 * <p>
	 * Convenience properties are mapped to Modules, for cloning the convenience
	 * properties can be ignored as the will be copied as part of the module
	 * cloning.
	 */
	public static final Set<String> CONVENIENCE_PROPERTIES = Collections
			.unmodifiableSet(IGNORE_PROPERTIES);

	static {

		IGNORE_PROPERTIES.add("publishedDate");
		IGNORE_PROPERTIES.add("author");

		final Map<String, Class<?>> basePropInterfaceMap = new HashMap<String, Class<?>>();
		basePropInterfaceMap.put("type", String.class);
		basePropInterfaceMap.put("subtitle", String.class);
		basePropInterfaceMap.put("uri", String.class);
		basePropInterfaceMap.put("title", String.class);
		basePropInterfaceMap.put("link", String.class);
		basePropInterfaceMap.put("uri", String.class);
		basePropInterfaceMap.put("description", SyndContent.class);
		basePropInterfaceMap.put("contents", SyndContent.class);
		basePropInterfaceMap.put("enclosures", SyndEnclosure.class);
		basePropInterfaceMap.put("modules", Module.class);
		basePropInterfaceMap.put("categories", SyndCategory.class);

		final Map<Class<? extends CopyFrom>, Class<?>> basePropClassImplMap = new HashMap<Class<? extends CopyFrom>, Class<?>>();
		basePropClassImplMap.put(SyndContent.class, SyndContentImpl.class);
		basePropClassImplMap.put(SyndEnclosure.class, SyndEnclosureImpl.class);
		basePropClassImplMap.put(SyndCategory.class, SyndCategoryImpl.class);
		basePropClassImplMap.put(DCModule.class, DCModuleImpl.class);
		basePropClassImplMap.put(SyModule.class, SyModuleImpl.class);

		COPY_FROM_HELPER = new CopyFromHelper(SyndEntry.class,
				basePropInterfaceMap, basePropClassImplMap);

	}

	protected SniperEntryImpl(final Class<?> beanClass,
			final Set<String> convenienceProperties) {
		objBean = new ObjectBean(beanClass, this, convenienceProperties);
	}

	public SniperEntryImpl() {
		this(SyndEntry.class, IGNORE_PROPERTIES);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return objBean.clone();
	}

	@Override
	public Class<? extends CopyFrom> getInterface() {
		return SyndEntry.class;
	}

	@Override
	public void copyFrom(CopyFrom obj) {
		COPY_FROM_HELPER.copy(this, obj);

	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSubtitle() {
		return subtitle;
	}

	@Override
	public String getUri() {

		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;

	}

	@Override
	public String getTitle() {
		if (title != null) {
			return title.getValue();
		}
		return null;
	}

	@Override
	public void setTitle(String title) {
		if (this.title == null) {
			this.title = new SyndContentImpl();
		}
		this.title.setValue(title);

	}

	@Override
	public SyndContent getTitleEx() {
		return title;
	}

	@Override
	public void setTitleEx(SyndContent title) {
		this.title = title;

	}

	@Override
	public String getLink() {
		return link;
	}

	@Override
	public void setLink(String link) {
		this.link = link;

	}

	@Override
	public List<SyndLink> getLinks() {
		return links = Lists.createWhenNull(links);
	}

	@Override
	public void setLinks(List<SyndLink> links) {
		this.links = links;

	}

	@Override
	public SyndContent getDescription() {

		return description;
	}

	@Override
	public void setDescription(SyndContent description) {
		this.description = description;

	}

	@Override
	public List<SyndContent> getContents() {
		return contents;
	}

	@Override
	public void setContents(List<SyndContent> contents) {
		this.contents = contents;

	}

	@Override
	public List<SyndEnclosure> getEnclosures() {
		return enclosures;
	}

	@Override
	public void setEnclosures(List<SyndEnclosure> enclosures) {
		this.enclosures = enclosures;

	}

	@Override
	public Date getPublishedDate() {
		return getDCModule().getDate();
	}

	@Override
	public void setPublishedDate(Date publishedDate) {
		getDCModule().setDate(publishedDate);

	}

	@Override
	public Date getUpdatedDate() {
		return Dates.copy(updatedDate);
	}

	@Override
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = new Date(updatedDate.getTime());

	}

	@Override
	public List<SyndPerson> getAuthors() {
		return authors = Lists.createWhenNull(authors);
	}

	@Override
	public void setAuthors(List<SyndPerson> authors) {
		this.authors = authors;

	}

	@Override
	public String getAuthor() {
		String author;

		// Start out looking for one or more authors in authors. For non-Atom
		// feeds, authors may actually be null.
		if (Lists.isNotEmpty(authors)) {
			author = authors.get(0).getName();
		} else {
			author = getDCModule().getCreator();
		}

		if (author == null) {
			author = "";
		}

		return author;

	}

	@Override
	public void setAuthor(String author) {
		// Get the DCModule so that we can check to see if "creator" is already
		// set.
		final DCModule dcModule = getDCModule();
		final String currentValue = dcModule.getCreator();

		if (Strings.isEmpty(currentValue)) {
			getDCModule().setCreator(author);
		}

	}

	@Override
	public List<SyndPerson> getContributors() {
		return contributors = Lists.createWhenNull(contributors);
	}

	@Override
	public void setContributors(final List<SyndPerson> contributors) {
		this.contributors = contributors;
	}

	@Override
	public List<SyndCategory> getCategories() {
		return categories;
	}

	@Override
	public void setCategories(List<SyndCategory> categories) {
		this.categories = categories;
	}

	@Override
	public SyndFeed getSource() {
		return source;
	}

	@Override
	public void setSource(SyndFeed source) {
		this.source = source;

	}

	@Override
	public Object getWireEntry() {
		return wireEntry;
	}

	@Override
	public Module getModule(String uri) {
		return ModuleUtils.getModule(getModules(), uri);
	}

	/**
	 * Returns the Dublin Core module of the feed.
	 * 
	 * @return the DC module, it's never <b>null</b>
	 * 
	 */
	private DCModule getDCModule() {
		return (DCModule) getModule(DCModule.URI);
	}

	@Override
	public List<Module> getModules() {
		modules = Lists.createWhenNull(modules);
		if (ModuleUtils.getModule(modules, DCModule.URI) == null) {
			modules.add(new DCModuleImpl());
		}
		return modules;
	}

	@Override
	public void setModules(List<Module> modules) {
		this.modules = modules;

	}

	@Override
	public List<Element> getForeignMarkup() {
		return foreignMarkup = Lists.createWhenNull(foreignMarkup);
	}

	@Override
	public void setForeignMarkup(List<Element> foreignMarkup) {
		this.foreignMarkup = foreignMarkup;

	}

	@Override
	public String getComments() {
		return comments;
	}

	@Override
	public void setComments(String comments) {
		this.comments = comments;

	}

	@Override
	public SyndLink findRelatedLink(String relation) {
		final List<SyndLink> syndLinks = getLinks();
		for (final SyndLink syndLink : syndLinks) {
			if (relation.equals(syndLink.getRel())) {
				return syndLink;
			}
		}
		return null;
	}

}
