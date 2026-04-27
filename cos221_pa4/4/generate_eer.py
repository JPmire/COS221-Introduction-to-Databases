#!/usr/bin/env python3
"""Generate a draw.io EER diagram for the Chinook database in COS 221 Chen's notation."""

# Style constants matching previous practicals
ENT = 'whiteSpace=wrap;html=1;align=center;labelBackgroundColor=none;fillColor=#A8DADC;strokeColor=#457B9D;fontColor=#1D3557;'
ATTR = 'ellipse;whiteSpace=wrap;html=1;align=center;labelBackgroundColor=none;fillColor=#A8DADC;strokeColor=#457B9D;fontColor=#1D3557;'
PK_ATTR = 'ellipse;whiteSpace=wrap;html=1;align=center;fontStyle=4;labelBackgroundColor=none;fillColor=#A8DADC;strokeColor=#457B9D;fontColor=#1D3557;'
REL = 'shape=rhombus;perimeter=rhombusPerimeter;whiteSpace=wrap;html=1;align=center;labelBackgroundColor=none;fillColor=#A8DADC;strokeColor=#457B9D;fontColor=#1D3557;'
EDGE = 'endArrow=none;html=1;rounded=0;labelBackgroundColor=none;strokeColor=#457B9D;fontColor=default;'
TOTAL_EDGE = 'shape=link;html=1;rounded=0;labelBackgroundColor=none;strokeColor=#457B9D;fontColor=default;'
LBL = 'resizable=0;html=1;whiteSpace=wrap;align=right;verticalAlign=bottom;labelBackgroundColor=none;fillColor=#A8DADC;strokeColor=#457B9D;fontColor=#1D3557;'
SELF_REL_EDGE = 'endArrow=none;html=1;rounded=0;labelBackgroundColor=none;strokeColor=#457B9D;fontColor=default;curved=1;'

cells = []
cell_id = [2]  # mutable counter

def nid():
    cell_id[0] += 1
    return str(cell_id[0])

def entity(name, x, y, w=120, h=50):
    i = nid()
    cells.append(f'        <mxCell id="{i}" value="{name}" style="{ENT}" vertex="1" parent="1"><mxGeometry x="{x}" y="{y}" width="{w}" height="{h}" as="geometry" /></mxCell>')
    return i

def attr(name, x, y, is_pk=False, w=120, h=40):
    i = nid()
    s = PK_ATTR if is_pk else ATTR
    cells.append(f'        <mxCell id="{i}" value="{name}" style="{s}" vertex="1" parent="1"><mxGeometry x="{x}" y="{y}" width="{w}" height="{h}" as="geometry" /></mxCell>')
    return i

def relationship(name, x, y, w=140, h=80):
    i = nid()
    cells.append(f'        <mxCell id="{i}" value="{name}" style="{REL}" vertex="1" parent="1"><mxGeometry x="{x}" y="{y}" width="{w}" height="{h}" as="geometry" /></mxCell>')
    return i

def edge(src, tgt):
    i = nid()
    cells.append(f'        <mxCell id="{i}" style="{EDGE}" edge="1" parent="1" source="{src}" target="{tgt}"><mxGeometry relative="1" as="geometry" /></mxCell>')
    return i

def rel_edge(src, tgt, card, total=False):
    """Edge from entity to relationship with cardinality label. total=True for double line (total participation)."""
    i = nid()
    s = TOTAL_EDGE if total else EDGE
    cells.append(f'        <mxCell id="{i}" style="{s}" edge="1" parent="1" source="{src}" target="{tgt}"><mxGeometry relative="1" as="geometry" /></mxCell>')
    li = nid()
    cells.append(f'        <mxCell id="{li}" connectable="0" parent="{i}" style="{LBL}" value="{card}" vertex="1"><mxGeometry relative="1" x="1" as="geometry"><mxPoint y="-10" as="offset" /></mxGeometry></mxCell>')

def self_rel_edge(src, tgt, card, pts=None):
    i = nid()
    pts_xml = ""
    if pts:
        pts_xml = '<Array as="points">' + ''.join(f'<mxPoint x="{p[0]}" y="{p[1]}" />' for p in pts) + '</Array>'
    cells.append(f'        <mxCell id="{i}" style="{SELF_REL_EDGE}" edge="1" parent="1" source="{src}" target="{tgt}"><mxGeometry relative="1" as="geometry">{pts_xml}</mxGeometry></mxCell>')
    li = nid()
    cells.append(f'        <mxCell id="{li}" connectable="0" parent="{i}" style="{LBL}" value="{card}" vertex="1"><mxGeometry relative="1" x="1" as="geometry"><mxPoint y="-10" as="offset" /></mxGeometry></mxCell>')


# ── LAYOUT ──
# Central: Track at center, surrounded by related entities
# Row 1 (top):     Artist, Album, Track, Genre, MediaType
# Row 2 (middle):  Playlist -- M:N -- Track,  Employee (self-ref) -- supports -- Customer
# Row 3 (bottom):  Customer -- Invoice -- InvoiceLine

# Coordinates
# Track is the hub

TX, TY = 1600, 900   # Track

ArtX, ArtY = 400, 300    # Artist
AlbX, AlbY = 1000, 300   # Album

GenX, GenY = 2200, 300    # Genre
MTX, MTY = 2700, 900     # MediaType

PLX, PLY = 1600, 200     # Playlist

EmpX, EmpY = 200, 900    # Employee
CusX, CusY = 600, 900    # Customer

InvX, InvY = 600, 1500   # Invoice
ILX, ILY = 1200, 1500    # InvoiceLine

# ── ENTITIES ──
e_track = entity("TRACK", TX, TY)
e_artist = entity("ARTIST", ArtX, ArtY)
e_album = entity("ALBUM", AlbX, AlbY)
e_genre = entity("GENRE", GenX, GenY)
e_media = entity("MEDIA_TYPE", MTX, MTY, w=130)
e_playlist = entity("PLAYLIST", PLX, PLY)
e_employee = entity("EMPLOYEE", EmpX, EmpY)
e_customer = entity("CUSTOMER", CusX, CusY)
e_invoice = entity("INVOICE", InvX, InvY)
e_invoiceline = entity("INVOICE_LINE", ILX, ILY, w=130)

# ── ATTRIBUTES ──

# --- Artist ---
a_art_id = attr("ArtistId", ArtX-100, ArtY-80, is_pk=True)
edge(e_artist, a_art_id)
a_art_name = attr("Name", ArtX+130, ArtY-60)
edge(e_artist, a_art_name)

# --- Album ---
a_alb_id = attr("AlbumId", AlbX-80, AlbY-80, is_pk=True)
edge(e_album, a_alb_id)
a_alb_title = attr("Title", AlbX+130, AlbY-60)
edge(e_album, a_alb_title)

# --- Track ---
a_tr_id = attr("TrackId", TX-160, TY-80, is_pk=True)
edge(e_track, a_tr_id)
a_tr_name = attr("Name", TX+140, TY-80)
edge(e_track, a_tr_name)
a_tr_comp = attr("Composer", TX+160, TY+10)
edge(e_track, a_tr_comp)
a_tr_ms = attr("Milliseconds", TX+160, TY+60)
edge(e_track, a_tr_ms)
a_tr_bytes = attr("Bytes", TX+140, TY+110)
edge(e_track, a_tr_bytes)
a_tr_price = attr("UnitPrice", TX-160, TY+80)
edge(e_track, a_tr_price)

# --- Genre ---
a_gen_id = attr("GenreId", GenX-80, GenY-80, is_pk=True)
edge(e_genre, a_gen_id)
a_gen_name = attr("Name", GenX+130, GenY-60)
edge(e_genre, a_gen_name)

# --- MediaType ---
a_mt_id = attr("MediaTypeId", MTX-30, MTY-90, is_pk=True, w=130)
edge(e_media, a_mt_id)
a_mt_name = attr("Name", MTX+140, MTY-60)
edge(e_media, a_mt_name)

# --- Playlist ---
a_pl_id = attr("PlaylistId", PLX-150, PLY-60, is_pk=True)
edge(e_playlist, a_pl_id)
a_pl_name = attr("Name", PLX+140, PLY-60)
edge(e_playlist, a_pl_name)

# --- Employee ---
a_emp_id = attr("EmployeeId", EmpX-160, EmpY-90, is_pk=True, w=120)
edge(e_employee, a_emp_id)
a_emp_ln = attr("LastName", EmpX-170, EmpY-30)
edge(e_employee, a_emp_ln)
a_emp_fn = attr("FirstName", EmpX-170, EmpY+30)
edge(e_employee, a_emp_fn)
a_emp_title = attr("Title", EmpX-170, EmpY+90)
edge(e_employee, a_emp_title)
a_emp_bd = attr("BirthDate", EmpX-80, EmpY+140)
edge(e_employee, a_emp_bd)
a_emp_hd = attr("HireDate", EmpX+40, EmpY+140)
edge(e_employee, a_emp_hd)
a_emp_addr = attr("Address", EmpX+130, EmpY+120)
edge(e_employee, a_emp_addr)
a_emp_city = attr("City", EmpX+130, EmpY+60)
edge(e_employee, a_emp_city)
a_emp_state = attr("State", EmpX+10, EmpY-100)
edge(e_employee, a_emp_state)
a_emp_country = attr("Country", EmpX-60, EmpY-140)
edge(e_employee, a_emp_country)
a_emp_pc = attr("PostalCode", EmpX+60, EmpY-140)
edge(e_employee, a_emp_pc)
a_emp_phone = attr("Phone", EmpX+150, EmpY-60)
edge(e_employee, a_emp_phone)
a_emp_fax = attr("Fax", EmpX+150, EmpY)
edge(e_employee, a_emp_fax)
a_emp_email = attr("Email", EmpX+140, EmpY-120)
edge(e_employee, a_emp_email)

# --- Customer ---
a_cus_id = attr("CustomerId", CusX-80, CusY-100, is_pk=True)
edge(e_customer, a_cus_id)
a_cus_fn = attr("FirstName", CusX-140, CusY-40)
edge(e_customer, a_cus_fn)
a_cus_ln = attr("LastName", CusX-140, CusY+20)
edge(e_customer, a_cus_ln)
a_cus_comp = attr("Company", CusX-140, CusY+80)
edge(e_customer, a_cus_comp)
a_cus_addr = attr("Address", CusX+140, CusY+80)
edge(e_customer, a_cus_addr)
a_cus_city = attr("City", CusX+140, CusY+20)
edge(e_customer, a_cus_city)
a_cus_state = attr("State", CusX+140, CusY-40)
edge(e_customer, a_cus_state)
a_cus_country = attr("Country", CusX+40, CusY-100)
edge(e_customer, a_cus_country)
a_cus_pc = attr("PostalCode", CusX+150, CusY-100)
edge(e_customer, a_cus_pc)
a_cus_phone = attr("Phone", CusX+140, CusY+140)
edge(e_customer, a_cus_phone)
a_cus_fax = attr("Fax", CusX+20, CusY+140)
edge(e_customer, a_cus_fax)
a_cus_email = attr("Email", CusX-100, CusY+140)
edge(e_customer, a_cus_email)

# --- Invoice ---
a_inv_id = attr("InvoiceId", InvX-140, InvY-80, is_pk=True)
edge(e_invoice, a_inv_id)
a_inv_date = attr("InvoiceDate", InvX-140, InvY-20)
edge(e_invoice, a_inv_date)
a_inv_baddr = attr("BillingAddress", InvX-160, InvY+40, w=130)
edge(e_invoice, a_inv_baddr)
a_inv_bcity = attr("BillingCity", InvX-150, InvY+100)
edge(e_invoice, a_inv_bcity)
a_inv_bstate = attr("BillingState", InvX-50, InvY+140)
edge(e_invoice, a_inv_bstate)
a_inv_bcountry = attr("BillingCountry", InvX+60, InvY+140, w=130)
edge(e_invoice, a_inv_bcountry)
a_inv_bpc = attr("BillingPostalCode", InvX+130, InvY+100, w=140)
edge(e_invoice, a_inv_bpc)
a_inv_total = attr("Total", InvX+140, InvY+40)
edge(e_invoice, a_inv_total)

# --- InvoiceLine ---
a_il_id = attr("InvoiceLineId", ILX+10, ILY-80, is_pk=True, w=130)
edge(e_invoiceline, a_il_id)
a_il_up = attr("UnitPrice", ILX+150, ILY-40)
edge(e_invoiceline, a_il_up)
a_il_qty = attr("Quantity", ILX+150, ILY+30)
edge(e_invoiceline, a_il_qty)


# ── RELATIONSHIPS ──

# R1: Artist --1:N-- Album  (Produces)
r_produces = relationship("Produces", 680, 260, w=140, h=80)
rel_edge(e_artist, r_produces, "1")
rel_edge(e_album, r_produces, "N", total=True)

# R2: Album --1:N-- Track  (Contains)
r_contains = relationship("Contains", 1280, 580, w=140, h=80)
rel_edge(e_album, r_contains, "1")
rel_edge(e_track, r_contains, "N")

# R3: Genre --1:N-- Track  (Classifies)
r_classifies = relationship("Classifies", 1900, 500, w=140, h=80)
rel_edge(e_genre, r_classifies, "1")
rel_edge(e_track, r_classifies, "N")

# R4: MediaType --1:N-- Track  (Encodes)
r_encodes = relationship("Encodes", 2200, 900, w=140, h=80)
rel_edge(e_media, r_encodes, "1")
rel_edge(e_track, r_encodes, "N", total=True)

# R5: Playlist --M:N-- Track  (PlaylistTrack => resolved as M:N relationship)
r_playlist_track = relationship("Contains_Track", 1600, 580, w=150, h=80)
rel_edge(e_playlist, r_playlist_track, "M", total=True)
rel_edge(e_track, r_playlist_track, "N", total=True)

# R6: Employee --1:N-- Customer  (Supports)
r_supports = relationship("Supports", 400, 860, w=140, h=80)
rel_edge(e_employee, r_supports, "1")
rel_edge(e_customer, r_supports, "N")

# R7: Employee self-referential (ReportsTo)
r_reports = relationship("ReportsTo", 100, 600, w=140, h=80)
self_rel_edge(e_employee, r_reports, "1", pts=[(EmpX, EmpY-30), (EmpX-160, EmpY-30), (EmpX-160, 640)])
self_rel_edge(e_employee, r_reports, "N", pts=[(EmpX+60, EmpY-30), (EmpX+60, 700), (240, 700)])

# R8: Customer --1:N-- Invoice  (Places)
r_places = relationship("Places", 560, 1180, w=140, h=80)
rel_edge(e_customer, r_places, "1")
rel_edge(e_invoice, r_places, "N", total=True)

# R9: Invoice --1:N-- InvoiceLine  (Has_Line)
r_hasline = relationship("Has_Line", 900, 1460, w=140, h=80)
rel_edge(e_invoice, r_hasline, "1")
rel_edge(e_invoiceline, r_hasline, "N", total=True)

# R10: Track --1:N-- InvoiceLine  (Purchased_In)
r_purchased = relationship("Purchased_In", 1500, 1200, w=150, h=80)
rel_edge(e_track, r_purchased, "1")
rel_edge(e_invoiceline, r_purchased, "N", total=True)


# ── BUILD XML ──
xml = f'''<?xml version="1.0" encoding="UTF-8"?>
<mxfile host="app.diagrams.net" agent="Mozilla/5.0" version="29.6.1">
  <diagram id="chinook_eer" name="Chinook EER Diagram">
    <mxGraphModel dx="2000" dy="2000" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="4000" pageHeight="3000" math="0" shadow="0">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
{chr(10).join(cells)}
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
'''

out = "/Users/jpjoubert/Library/CloudStorage/GoogleDrive-jpjoubert2006@gmail.com/My Drive/University of Pretoria/Year 2/Modules/COS 221/Practicals/4/Chinook_EER.drawio"
with open(out, 'w', encoding='utf-8') as f:
    f.write(xml)

print(f"Written to: {out}")
print(f"Total cells: {len(cells)}")
