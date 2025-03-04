-- Insert SponsorOrganizations
INSERT INTO SponsorOrganizations (sponsorOrganizationName, sponsorOrganizationType, sponsorOrganizationAddress, sponsorOrganizationNif, sponsorOrganizationVat) VALUES
('Tech Corp', 'private', '123 Tech Street', 'A12345678', 'VAT123'),
('City Council', 'public', '456 Main Square', 'B87654321', 'VAT456');

-- Insert GBMembers
INSERT INTO GBMembers (gbMemberName, gbMemberRole) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer'),
('Charlie Brown', 'Secretary'),
('Diana Prince', 'Vice President');

-- Insert SponsorContacts
INSERT INTO SponsorContacts (sponsorContactSponsorOrganizationId, sponsorContactName, sponsorContactEmail, sponsorContactPhone) VALUES
(1, 'John Doe', 'john@techcorp.com', '555-1234'),
(2, 'Mary White', 'mary@citycouncil.gov', '555-5678'),
(1, 'Jane Roe', 'jane@techcorp.com', '555-4321'),
(2, 'Peter Black', 'peter@citycouncil.gov', '555-8765');

-- Insert SponsorshipAgreements
INSERT INTO SponsorshipAgreements (sponsorshipAgreementSponsorContactId, sponsorshipAgreementGBMemberId, sponsorshipAgreementActivityId, sponsorshipAgreementAmount, sponsorshipAgreementDate, sponsorshipAgreementStatus) VALUES
(1, 1, 1, 5000.00, '2024-01-15', 'signed'),
(2, 2, 2, 2000.00, '2024-02-01', 'closed');

-- Insert Invoices
INSERT INTO Invoices (invoiceSponsorshipAgreementId, invoiceDateIssued, invoiceDateSent, invoiceDateExpiration, invoiceTotalAmount, invoiceTaxRate, invoiceStatus) VALUES
(1, '2024-01-20', '2024-01-22', '2024-02-20', 5500.00, 10, 'issued'),
(2, '2024-02-05', NULL, '2024-03-05', 2200.00, 10, 'draft'),
(1, '2024-03-10', '2024-03-12', '2024-04-10', 3300.00, 10, 'issued'),
(2, '2024-04-15', '2024-04-17', '2024-05-15', 4400.00, 10, 'issued');

-- Insert SponsorshipPayments
INSERT INTO SponsorshipPayments (sponsorshipPaymentInvoiceId, sponsorshipPaymentDate, sponsorshipPaymentAmount) VALUES
(1, '2024-01-25', 5000.00),
(2, '2024-02-10', 2000.00);

-- Insert Activities
INSERT INTO Activities (activityName, activityStatus, activityDateStart, activityDateEnd, activityPlace) VALUES
('Olimpics 2024', 'planned', '2024-03-10', '2024-03-12', 'Convention Center'),
('ImpulsoTIC Week 2024', 'done', '2024-02-15', '2024-02-15', 'Central Park');

-- Insert Levels
INSERT INTO Levels (levelName, levelFee) VALUES
('Basic', 50.00);

-- Insert ActivityLevels
INSERT INTO ActivityLevels (activityLevelActivityId, activityLevelLevelId) VALUES
(1, 1),
(2, 1);

-- Insert Movements
INSERT INTO Movements (movementActivityId, movementType, movementConcept, movementAmount, movementDate, movementReceiptNumber, movementStatus) VALUES
(1, 'income', 'Sponsorship Payment', 5000.00, '2024-01-25', 'INV-001', 'paid'),
(2, 'expense', 'Cleanup Supplies', 500.00, '2024-02-10', 'EXP-001', 'paid'),
(3, 'income', 'Ticket Sales', 3000.00, '2024-03-15', 'INV-002', 'paid'),
(4, 'expense', 'Marketing Materials', 1000.00, '2024-04-20', 'EXP-002', 'paid'),
(5, 'income', 'Registration Fees', 1500.00, '2024-05-25', 'INV-003', 'paid'),
(1, 'expense', 'Venue Rental', 2000.00, '2024-06-01', 'EXP-003', 'paid');