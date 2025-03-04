DROP TABLE IF EXISTS Movements;
DROP TABLE IF EXISTS ActivityLevels;
DROP TABLE IF EXISTS Activities;
DROP TABLE IF EXISTS Levels;
DROP TABLE IF EXISTS SponsorshipPayments;
DROP TABLE IF EXISTS Invoices;
DROP TABLE IF EXISTS SponsorshipAgreements;
DROP TABLE IF EXISTS SponsorContacts;
DROP TABLE IF EXISTS GBMembers;
DROP TABLE IF EXISTS SponsorOrganizations;

PRAGMA foreign_keys = ON;

CREATE TABLE SponsorOrganizations (
    sponsorOrganizationId INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsorOrganizationName TEXT NOT NULL,
    sponsorOrganizationType TEXT NOT NULL CHECK(lower(sponsorOrganizationType) IN ('private corp.', 'public administration')),
    sponsorOrganizationAddress TEXT,
    sponsorOrganizationNif TEXT,
    sponsorOrganizationVat TEXT
);

CREATE TABLE GBMembers (
    gbMemberId INTEGER PRIMARY KEY AUTOINCREMENT,
    gbMemberName TEXT NOT NULL,
    gbMemberRole TEXT UNIQUE NOT NULL
);

CREATE TABLE SponsorContacts (
    sponsorContactId INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsorContactSponsorOrganizationId INTEGER NOT NULL,
    sponsorContactName TEXT NOT NULL,
    sponsorContactEmail TEXT NOT NULL,
    sponsorContactPhone TEXT,
    FOREIGN KEY (sponsorContactSponsorOrganizationId) REFERENCES SponsorOrganizations(sponsorOrganizationId) ON DELETE CASCADE
);

CREATE TABLE SponsorshipAgreements (
    sponsorshipAgreementId INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsorshipAgreementSponsorContactId INTEGER NOT NULL,
    sponsorshipAgreementGBMemberId INTEGER NOT NULL,
    sponsorshipAgreementActivityId TEXT NOT NULL,
    sponsorshipAgreementAmount REAL NOT NULL,
    sponsorshipAgreementDate TEXT NOT NULL,
    sponsorshipAgreementStatus TEXT NOT NULL CHECK (lower(sponsorshipAgreementStatus) IN ('signed', 'closed', 'modified', 'cancelled')),
    FOREIGN KEY (sponsorshipAgreementSponsorContactId) REFERENCES SponsorContacts(sponsorContactId) ON DELETE CASCADE,
    FOREIGN KEY (sponsorshipAgreementGBMemberId) REFERENCES GBMembers(gbMemberId) ON DELETE CASCADE,
    FOREIGN KEY (sponsorshipAgreementActivityId) REFERENCES Activities(activityId) ON DELETE CASCADE
);

CREATE TABLE Invoices (
    invoiceId INTEGER PRIMARY KEY AUTOINCREMENT,
    invoiceSponsorshipAgreementId INTEGER NOT NULL,
    invoiceDateIssued TEXT NOT NULL,
    invoiceDateSent TEXT,
    invoiceDateExpiration TEXT,
    invoiceTotalAmount REAL NOT NULL,
    invoiceTaxRate REAL NOT NULL,
    invoiceStatus TEXT NOT NULL CHECK (lower(invoiceStatus) IN ('draft', 'issued', 'paid', 'rectified', 'cancelled')),
    FOREIGN KEY (invoiceSponsorshipAgreementId) REFERENCES SponsorshipAgreements(sponsorshipAgreementId) ON DELETE CASCADE
);

CREATE TABLE SponsorshipPayments (
    sponsorshipPaymentId INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsorshipPaymentInvoiceId INTEGER NOT NULL,
    sponsorshipPaymentDate TEXT NOT NULL,
    sponsorshipPaymentAmount REAL NOT NULL,
    FOREIGN KEY (sponsorshipPaymentInvoiceId) REFERENCES Invoices(invoiceId) ON DELETE CASCADE
);

CREATE TABLE Activities (
    activityId INTEGER PRIMARY KEY AUTOINCREMENT,
    activityName TEXT UNIQUE NOT NULL,
    activityStatus TEXT NOT NULL CHECK (lower(activityStatus) IN ('registered', 'planned', 'cancelled', 'done')),
    activityDateStart TEXT,
    activityDateEnd TEXT,
    activityPlace TEXT
);

CREATE TABLE ActivityLevels (
    activityLevelId INTEGER PRIMARY KEY AUTOINCREMENT,
    activityLevelActivityId INTEGER NOT NULL,
    activityLevelLevelId INTEGER NOT NULL,
    FOREIGN KEY (activityLevelActivityId) REFERENCES Activities(activityId) ON DELETE CASCADE,
    FOREIGN KEY (activityLevelLevelId) REFERENCES Levels(levelId) ON DELETE CASCADE
);

CREATE TABLE Levels (
    levelId INTEGER PRIMARY KEY AUTOINCREMENT,
    levelName TEXT NOT NULL,
    levelFee REAL NOT NULL
);

CREATE TABLE Movements (
    movementId INTEGER PRIMARY KEY AUTOINCREMENT,
    movementActivityId INTEGER NOT NULL,
    movementType TEXT NOT NULL CHECK (lower(movementType) IN ('income', 'expense')),
    movementConcept TEXT NOT NULL,
    movementAmount REAL NOT NULL,
    movementDate TEXT,
    movementReceiptNumber TEXT,
    movementStatus TEXT NOT NULL CHECK(lower(movementStatus) IN ('estimated', 'cancelled', 'paid')),
    FOREIGN KEY (movementActivityId) REFERENCES Activities(activityId) ON DELETE CASCADE
);