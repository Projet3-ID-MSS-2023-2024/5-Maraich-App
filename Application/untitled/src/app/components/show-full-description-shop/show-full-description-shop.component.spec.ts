import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowFullDescriptionShopComponent } from './show-full-description-shop.component';

describe('ShowFullDescriptionShopComponent', () => {
  let component: ShowFullDescriptionShopComponent;
  let fixture: ComponentFixture<ShowFullDescriptionShopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowFullDescriptionShopComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShowFullDescriptionShopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
