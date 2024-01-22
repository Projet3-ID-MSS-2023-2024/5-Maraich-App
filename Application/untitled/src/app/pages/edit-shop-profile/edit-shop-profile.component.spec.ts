import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditShopProfileComponent } from './edit-shop-profile.component';

describe('EditShopProfileComponent', () => {
  let component: EditShopProfileComponent;
  let fixture: ComponentFixture<EditShopProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditShopProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditShopProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
